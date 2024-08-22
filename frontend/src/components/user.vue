<template>
  <div class="dashboard-container">
    <el-form :model="tableData" label-width="80px" class="el-form--inline">
      <el-form-item label="用户名称">
        <el-input
            v-model="tableData.name"
            placeholder="请输入用户名称"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
            v-model="tableData.minCreateTime"
            type="datetime"
            placeholder="起始时间"
            class="date-picker"
        />
        <el-date-picker
            v-model="tableData.maxCreateTime"
            type="datetime"
            placeholder="截止时间"
            class="date-picker"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getUserList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!--  用户操作按钮  -->
    <div>
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleCreate">新增</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleBatchDelete">删除</el-button>
    </div>
    <!--  用户列表  -->
    <el-table
        :data="tableData.list"
    >
      <el-table-column type="index" width="60" />
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" sortable="custion" />
      <el-table-column prop="userName" label="用户名" sortable="custom" />
      <el-table-column prop="createdAt" label="创建时间" sortable="custom" />
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  分页  -->
    <el-pagination
        class="pagination"
        :current-page.sync="tableData.pageNum"
        :page-sizes="[5, 8, 10, 20]"
        :page-size.sync="tableData.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableData.total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
    />
    <!--  用户编辑/创建窗口  -->
    <el-dialog class="user-edit-dialog" :title="userEditForm.id?'用户编辑':'新增用户'" v-model="userEditDialogVisible" width="50%" top="8vh">
      <el-form
          ref="userEditForm"
          status-icon
          :model="userEditForm"
          label-width="80px"
          :rules="userEditForm.id ? userUpdateRules : userCreateRules"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userEditForm.userName" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="userEditDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdateUser">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import moment from "moment";

export default {
  name: 'User',
  data() {
    return {
      dialogVisible:true,
      tableData: {
        name: '',
        minCreateTime: '',
        maxCreateTime: '',
        list: [{
          id: 1,
          userName: '一汪小顺子',
          createdAt: '2024-08-21T16:01:12'
        }],
        selection: '',
        pageNum: 1,
        pageSize: 5,
        total: 1
      },
      userEditForm: {
        id: null, // 用于判断是编辑还是新增
        userName: '',
      },
      userEditDialogVisible: false,
      // 创建用户时的表单验证规则
      userCreateRules: {
        userName: [
          { required: true, message: '用户名是必填项', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符之间', trigger: 'blur' },
          // 其他规则...
        ],
      },

      // 编辑用户时的表单验证规则
      userUpdateRules: {
        userName: [
          { required: true, message: '用户名是必填项', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符之间', trigger: 'blur' },
          // 其他规则可以根据编辑场景不同进行调整...
        ],
      }
    };
  },
  created() {
    this.getUserList();
  },
  methods: {
    async getUserList() {
      try {
        const response = await axios.get('/users', {
          params: {
            // 字段
            userName: this.tableData.name,
            startDateTime: this.tableData.minCreateTime === "" ? "" : moment(this.tableData.minCreateTime).format("YYYY-MM-DDTHH:mm:ss"),
            endDateTime: this.tableData.maxCreateTime === "" ? "" : moment(this.tableData.maxCreateTime).format('YYYY-MM-DDTHH:mm:ss'),
            // 分页
            pageNum: this.tableData.pageNum,
            pageSize: this.tableData.pageSize
          }
        });
        console.log("minCreateTime:", this.tableData.minCreateTime);
        console.log("minCreateTime-f:", this.tableData.minCreateTime === "" ? "" : moment(this.tableData.minCreateTime).format("YYYY-MM-DDTHH:mm:ss"));
        console.log("maxCreateTime:", this.tableData.maxCreateTime);
        console.log("maxCreateTime-f:", this.tableData.maxCreateTime === "" ? "" : moment(this.tableData.maxCreateTime).format("YYYY-MM-DDTHH:mm:ss"));

        console.log("return:", response)
        console.log("return data:", response.data)
        // 假设 response.data 包含用户列表和总数
        this.tableData.list = response.data.list;
        console.log("return list:", response.data.list)
        this.tableData.total = response.data.total;
        console.log("return total:", response.data.total)
      } catch (error) {
        console.error('There was an error fetching the users:', error);
      }
    },
    handleCreate() {
      this.userEditForm.id = null
      this.userEditDialogVisible = true
    },
    handlePageChange(newPage) {
      this.tableData.pageNum = newPage
      this.getUserList()
    },
    handleSizeChange(newSize) {
      this.tableData.pageSize = newSize;
      this.tableData.pageNum = 1; // 重置到第一页
      this.getUserList();
    },
    handleEdit(row) {
      for (const key in this.userEditForm) {
        this.userEditForm[key] = row[key]
      }
      this.userEditDialogVisible = true
    },
    async addOrUpdateUser() {
      this.$refs.userEditForm.validate(async (valid) => {
        if (valid) {
          try {
            // 如果有 ID，说明是编辑操作
            if (this.userEditForm.id) {
              const response = await axios.put(`/users/${this.userEditForm.id}`, this.userEditForm);
              console.log('User updated:', response);
            } else {
              // 新增用户的逻辑
              const response = await axios.post(`/users`, this.userEditForm);
              console.log('User added:', response);
            }
            this.userEditDialogVisible = false;
            await this.getUserList()
          } catch (error) {
            console.error('There was an error updating the user:', error);
          }
        } else {
          console.log('Form validation failed!');
        }
      })
    },
    // 批量删除
    handleBatchDelete() {
      // tableData.selection id -> handleDelete
    },
    handleDelete(id) {
      this.$confirm('此操作永久删除该用户，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        // 对接删除用户接口
        const response = await axios.delete(`/users/${id}`);
        console.log('User deleted:', response);
        await this.getUserList()
      })
    },
    resetQuery() {
      this.tableData.name = ''
      this.tableData.minCreateTime = ''
      this.tableData.maxCreateTime = ''
    }
  }
};
</script>

<style scoped>

.date-picker{
  margin-right: 10px;
  width: 160px;
}
.dashboard-container{
  height: 100vh; /* 使容器占满整个视口高度 */
  margin-left: 5%;
  margin-right: 5%;
  max-width: 1200px; /* 可选：设置最大宽度以避免过宽 */
  padding: 20px; /* 可选：添加内边距 */
}
</style>

<template>
  <div>
    <div style="margin-bottom: 15px; margin-top: -15px; display: flex;">
      <el-button type="warning" @click="add">
        <i class="el-icon-plus"></i> 新增
      </el-button>
      <el-col style="text-align: right;">
        <el-input v-model= "id" placeholder="请输入用户名" suffix-icon="el-icon-search" style="width: 200px;"
                  @keyup.enter.native="loadPost"></el-input>
        <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </el-col>
    </div>
    <el-table
        style="width: 100%;"
        :data="tableData"
        :default-sort="{prop: 'id', order: 'descending'}"
        :cell-style="{'text-align':'center'}"
        :header-cell-style="{background: '#bdbabd', color: 'black', 'height': '60px', fontSize: '15px', textAlign: 'center'}"
    >
      <el-table-column prop="id" label="用户名" width="180">
      </el-table-column>
      <el-table-column prop="consumption" label="用户消费/元" width="180">
      </el-table-column>
      <el-table-column prop="latest" label="最后消费日期" width="250" sortable />
      <el-table-column prop="preference" label="用户偏好" width="150">
      </el-table-column>
      <el-table-column prop="point" width="150" label=" " sortable>
        <template slot="header">
          <el-tooltip class="item" effect="dark" content="1元 = 100积分" placement="top" >-->
            <i class="el-icon-info"></i>
          </el-tooltip>
          积分
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 10px">
            <el-button slot="reference" type="danger" size="small">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        style="background: #ffffff; height: 40px; display: flex; align-items: center; "
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>


    <!--新增表单-->
    <el-dialog
        title="订单信息"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px" style="display: block; align-items: center;">
        <el-form-item label="用户名" prop="id">
          <el-col :span="20">
            <el-input v-model="form.id"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="积分" prop="point">
          <el-col :span="20">
            <el-input-number v-model="form.point" :step="10"></el-input-number>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-left: 15px;
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>


<script>
export default {
  name: "membersManage",
  data() {
    return {
      allTableData: [],
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      id: '',
      centerDialogVisible: false,
      form: {
        id: '',
        consumption: '',
        latest: '',
        point: '0',
        preference: '',
      },
      cusColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 }
      ],
      rules: {
        id: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.form.resetFields();
    },
    mod(row) { // 编辑
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        // 赋值到表单
        this.form.id = row.id;
        this.form.consumption = row.consumption;
        this.form.point = row.point;
      });
    },
    del(id) { // 删除
      console.log(id);
      this.$axios.get(this.$httpUrl + '/members/del?id=' + id).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost();
        } else {
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      });
    },
    add() { // 新增
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        this.resetForm();
      });
    },
    handleSizeChange(val) { // 分页
      console.log(`每页 ${val} 条`);
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.loadPost();
    },
    doSave() {
      this.$axios.post(this.$httpUrl + '/members/save', this.form).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false;
          this.loadPost();
          this.resetForm();
        } else {
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      });
    },
    doMod() {
      this.$axios.post(this.$httpUrl + '/members/update', this.form).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false;
          this.loadPost();
          this.resetForm();
        } else {
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      });
    },
    save() { // 保存输入信息
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            this.doMod();
          } else {
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetParam() {
      this.name = '';
      this.loadPost();
    },
    loadPost() {
      this.$axios.post(this.$httpUrl + '/members/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          id: this.id,
        }
      }).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
          // 保存所有数据
          this.tableData = res.data;
          this.total = res.total;
        } else {
          alert('获取数据失败');
        }
      });
    }
  },
  beforeMount() {
    this.loadPost();
  }
}
</script>

<style scoped>
.el-table {
  width: 100%;
}
</style>
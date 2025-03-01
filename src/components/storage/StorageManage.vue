<template>
    <div>
      <div style="margin-bottom: 15px; margin-top: -15px; display: flex;">
        <el-button type="warning" @click=add>
          <i class="el-icon-plus"></i> 新增
        </el-button>
        <el-col style="text-align: right;">
          <el-input v-model="name" placeholder="请输入..." suffix-icon="el-icon-search" style="width: 200px;"
                    @keyup.enter.native="loadPost"></el-input>
          <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
          <el-button type="success;" @click="resetParam">重置</el-button>
        </el-col>
      </div>
      <el-table
          style="width: 100%;"
          :data="tableData"
          :default-sort="{prop: 'id', order: 'descending'}"
          :cell-style="{'text-align':'center'}"
          :header-cell-style="{background: '#bdbabd', color: 'black', 'height': '60px', fontSize: '15px', textAlign: 'center'}">}"
        >
        <el-table-column prop="id" label="ID" width="80">
        </el-table-column>
        <el-table-column prop="name" label="原料名称" width="180" />
        <el-table-column label="库存状态 " width="150" prop="condition"
                         :filters="[{ text: '需补货', value: 'danger' }, { text: '正常', value: 'success' }]"
                         :filter-method="filterTag"
                         filter-placement="bottom-end">
          <template #default="{row}">
            <el-tag
                :type="row.current < row.warning ? 'danger' : 'success'"
                effect="dark"
            >
              {{ row.current < row.warning ? '需补货' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="库存量">
          <template #default="{row}" >
            <span :class="{ 'warning-text': row.current < row.warning }">
              {{ row.current }} {{ row.unit }}
            </span>
            <div class="progress-container">
              <el-progress
                  :percentage="calcPercentage(row)"
                  :color="cusColors"
                  :show-text="false"
              />
              <span class="threshold">预警值: {{ row.warning }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" >
        </el-table-column>
        <el-table-column prop="operate" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>
            <el-popconfirm
                title="确定删除吗？"
                @confirm="del(scope.row.id)"
                style="margin-left: 10px"
            >
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

      <el-dialog
          title="库信息"
          :visible.sync="centerDialogVisible"
          width="30%"
          center>
        <el-form ref="form" :rules="rules" :model="form" label-width="80px" style="display: block; align-items: center;">
          <el-form-item label="原料名" prop="name">
            <el-col :span="20">
              <el-input v-model="form.name"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="当前库存" prop="current">
            <el-col :span="20">
              <el-input v-model="form.current"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="预警阈值" prop="warning">
            <el-col :span="20">
              <el-input v-model="form.warning"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-col :span="20">
              <el-input v-model="form.unit"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-col :span="20">
              <el-input type="textarea" v-model="form.remark"></el-input>
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

<script>

export default {
  name: "StorageManage",
  data() {
    /*let checkDuplicate = (rule, value, callback) => {
      if (this.form.id) {
        return callback();
      }
      this.$axios.get(this.$httpUrl + '/storage/findByName?name=' + this.form.name).then(res => res.data).then(res => {
        if (res.code != 200) {
          callback()
        } else {
          callback(new Error('当前原料已经存在'));
        }
      })
    };*/
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name: '',
      centerDialogVisible: false,
      form: {
        id: '',
        name: '',
        current:'',
        warning: '',
        unit: '',
        remark: '',
      },
      cusColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 }
      ],
      rules: {
        name: [
          {required: true, message: '请输入原料名', trigger: 'blur'},
          //{validator: checkDuplicate(), trigger: 'blur'}
        ],
        current: [
          {required: true, message: '请输入当前库存', trigger: 'blur'},
        ],
        warning: [
          {required: true, message: '请输入预警值', trigger: 'blur'},
        ],
        unit: [
          {required: true, message: '请输入单位', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.form.resetFields();
    },
    calcPercentage(row) {
      const max = Math.max(row.current, row.warning)
      return (row.current / max) * 100
    },
    filterTag(value, row) {
      return row.tagType === value;
    },
    mod(row) {//编辑
      this.centerDialogVisible = true
      this.$nextTick(() => {
        //赋值到表单
        this.form.id = row.id;
        this.form.name = row.name;
        this.form.current = row.current;
        this.form.warning = row.warning;
        this.form.unit = row.unit;
        this.form.remark = row.remark;
      })
    },
    del(id) {//删除
      console.log(id)
      this.$axios.get(this.$httpUrl + '/storage/del?id=' + id).then(res => res.data).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost()
        } else {
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      })
    },
    add() {//新增
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
      })
    },
    handleSizeChange(val) {//分页
      console.log(`每页 ${val} 条`);
      this.pageNum = 1
      this.pageSize = val;
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.pageNum = val
      this.loadPost()
    },
    doSave() {
      this.$axios.post(this.$httpUrl + '/storage/save', this.form).then(res => res.data).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this.resetForm()
        } else {
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      })
    },
    doMod() {
      this.$axios.post(this.$httpUrl + '/storage/update', this.form).then(res => res.data).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this.resetForm()
        } else {
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      })
    },
    save() {//保存输入信息
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
      this.name = ''
      this.loadPost()
    },
    loadPost() {
      this.$axios.post(this.$httpUrl + '/storage/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.name,
        }
      }).then(res => res.data).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.tableData = res.data
          this.total = res.total
        } else {
          alert('获取数据失败')
        }
      })
    }
  },
  beforeMount() {
    this.loadPost();
  }
}
</script>

<style scoped>
.el-table{
  width: 100%;

}
</style>
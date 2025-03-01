<template>
    <div>
      <div style="margin-bottom: 15px; margin-top: -15px; display: flex;">
        <el-col style="display: flex;">
          <el-button type="warning" @click="add">
            <i class="el-icon-plus"></i> 新增
          </el-button>
        </el-col>
        <el-col style="text-align: right;">
          <el-input v-model="username" placeholder="请输入..." suffix-icon="el-icon-search" style="width: 200px;"
                    @keyup.enter.native="loadPost"></el-input>
          <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 5px; width: 120px">
            <el-option
                v-for="item in sexs"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
          <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
          <el-button type="success;" @click="resetParam">重置</el-button>
        </el-col>
      </div>
      <el-table
          style="width: 100%;"
          :data="tableData"
          :cell-style="{'text-align':'center'}"
          :header-cell-style="{background: '#bdbabd', color: 'black', 'height': '60px', fontSize: '15px', textAlign: 'center'}">}"
        >
        <el-table-column prop="id" label="ID" width="80">
        </el-table-column>
        <el-table-column prop="account" label="账号" width="160">
        </el-table-column>
        <el-table-column prop="username" label="姓名" width="160">
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80">
        </el-table-column>
        <el-table-column prop="sex" label="性别" width="80">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.sex === 1 ? 'primary' : 'success'"
                disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" width="180">
        </el-table-column>
        <el-table-column prop="roleId" label="身份信息" width="160">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                disable-transitions>{{scope.row.roleId === 0 ? '管理员' :
                (scope.row.roleId === 1 ? '店长' : '员工')}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operate" label="操作" >
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
          :current-page="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog
          title="用户信息"
          :visible.sync="centerDialogVisible"
          width="30%"
          center>
        <el-form ref="form" :rules="rules" :model="form" label-width="80px" style="display: block; align-items: center;">
          <el-form-item label="账号" prop="account">
            <el-col :span="20">
              <el-input v-model="form.account"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="姓名" prop="username">
            <el-col :span="20">
              <el-input v-model="form.username"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-col :span="20">
              <el-input v-model="form.password"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-col :span="20">
              <el-input v-model="form.age"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="form.sex">
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-col :span="20">
              <el-input v-model="form.phone"></el-input>
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
  name: "AdminManage",
  data() {
    let checkAge = (rule, value, callback) => {
      if(value>150){
        callback(new Error('年龄输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.id){
        return callback();
      }
      this.$axios.get(this.$httpUrl+'/user/findByAccount?account=' + this.form.account).then(res=>res.data).then(res=>{
        if(res.code != 200){
          callback()
        }else{
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      tableData: [],
      pageSize:10,
      pageNum: 1,
      total: 0,
      username:'',
      sex:'',
      sexs:[
        {
          value: '0',
          label: '女'
        }, {
          value: '1',
          label: '男'
        }
      ],
      centerDialogVisible: false,
      form:{
        account:'',
        username:'',
        password:'',
        age:'',
        phone:'',
        sex:'0',
        roleId:'1',
      },
      rules: {
        account: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        username: [
          {required: true, message: '请输入名字', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输⼊年龄', trigger: 'blur'},
          {min: 1, max: 3, message: '⻓度在 1 到 3 个位', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "⼿机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger:"blur"}
        ]
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.form.resetFields();
    },
    mod(row){//编辑
      console.log(row)
      this.centerDialogVisible = true
      this.$nextTick(() => {
        //赋值到表单
        this.form = row
        this.form.age = row.age + ''
        this.form.sex = row.sex + ''
      })
    },
    del(id){//删除
      console.log(id)
      this.$axios.get(this.$httpUrl+'/user/del?id='+id).then(res=>res.data).then(res => {
        console.log(res)
        if(res.code == 200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost()
        }else{
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      })
    },
    add(){//新增
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
      })
    },
    handleSizeChange(val) {//分页
      console.log(`每页 ${val} 条`);
      this.pageNum = 1
      this.pageSize = val
      this.loadPost()
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/user/save', this.form).then(res=>res.data).then(res => {
        console.log(res)
        if(res.code == 200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this.resetForm()
        }else{
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/user/update', this.form).then(res=>res.data).then(res => {
        console.log(res)
        if(res.code == 200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this.resetForm()
        }else{
          this.$message({
            message: '操作失败!',
            type: 'error'
          });
        }
      })
    },
    save(){//保存输入信息
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            this.doSave();
          }

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.pageNum = val
      this.loadPost()
    },
    loadGet(){
      this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res => {
        console.log(res);
      })
    },
    resetParam(){
      this.username = ''
      this.sex=''
      this.loadPost()
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/user/listPageC', {
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          username:this.username,
          sex:this.sex,
          roleId:'1',
        }
      }).then(res=>res.data).then(res => {
        console.log(res)
        if(res.code === 200){
          this.tableData = res.data
          this.total = res.total
        }else{
          alert('获取数据失败')
        }
      })
    }
  },
  beforeMount() {
    //this.loadGet();
    this.loadPost();
  }
}
</script>

<style scoped>
.el-table{
  width: 100%;

}
</style>
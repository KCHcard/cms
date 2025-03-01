<template>
  <div>
    <div style="margin-bottom: 15px; margin-top: -15px; display: flex;">
      <el-button type="warning" @click="add">
        <i class="el-icon-plus"></i> 新增
      </el-button>
      <el-col style="text-align: right;">
        <el-input v-model="name" placeholder="请输入..." suffix-icon="el-icon-search" style="width: 200px;"
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
      <el-table-column type="expand"
      style="background-color: #9b4a9b;">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
            <el-form-item label="菜名">
              <span>{{ props.row.name }}</span>
            </el-form-item>
            <el-form-item label="所属类别">
              <span>{{ props.row.category }}</span>
            </el-form-item>
            <el-form-item label="菜品 ID">
              <span>{{ props.row.id }}</span>
            </el-form-item>
            <el-form-item label="价格">
              <span>{{ props.row.piece }}</span>
            </el-form-item>
            <el-form-item label="原料构成">
              <span>{{ props.row.composition }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column>

      </el-table-column>
      <el-table-column prop="name" label="菜品名称" width="250" />
      <el-table-column prop="dishcondition" label="菜品状态 " width="150" sortable
                       :filters="[{ text: '在售', value: 0 }, { text: '售罄', value: 1 }, { text: '下架', value: 2 }]"
                       :filter-method="filterTag"
                       filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.dishcondition === 0 ? 'success' : (scope.row.dishcondition === 1 ? 'primary' : 'danger')"
              disable-transitions>{{scope.row.dishcondition === 0 ? '在售' : (scope.row.dishcondition === 1 ? '售罄' : '下架')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="piece" label="菜品价格" width="150" sortable>
      </el-table-column>
      <el-table-column prop="operate" label="操作">
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


    <!--新增表单-->
    <el-dialog
        title="菜品信息"
        :visible.sync="centerDialogVisible"
        width="58%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px" label-position="top" style="display: block; align-items: center;">
        <el-form-item>
          <el-upload
              action="#"
              list-type="picture-card"
              :auto-upload="false">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img
                  class="el-upload-list__item-thumbnail"
                  :src="file.url" alt=""
              >
              <span class="el-upload-list__item-actions">
        <span
            class="el-upload-list__item-preview"
            @click="handlePictureCardPreview(file)"
        >
          <i class="el-icon-zoom-in"></i>
        </span>
        <span
            v-if="!disabled"
            class="el-upload-list__item-delete"
            @click="handleDownload(file)"
        >
          <i class="el-icon-download"></i>
        </span>
        <span
            v-if="!disabled"
            class="el-upload-list__item-delete"
            @click="handleRemove(file)"
        >
          <i class="el-icon-delete"></i>
        </span>
      </span>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="菜名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="菜品类别" prop="category">
          <el-radio-group v-model="form.category">
            <el-radio border label="凉菜"></el-radio>
            <el-radio border label="炒菜"></el-radio>
            <el-radio border label="汤食"></el-radio>
            <el-radio border label="点心"></el-radio>
            <el-radio border label="主食"></el-radio>
            <el-radio border label="煲类"></el-radio>
            <el-radio border label="酒水"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜品状态" prop="dishcondition">
          <el-radio-group v-model="form.dishcondition">
            <el-radio label="0">在售</el-radio>
            <el-radio label="1">售罄</el-radio>
            <el-radio label="2">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜品价格" prop="piece">
          <el-col :span="20">
            <el-input-number v-model="form.piece" :precision="2" :step="0.1" :max="100"></el-input-number>
          </el-col>
        </el-form-item>
        <el-form-item label="原料组成" prop="composition">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.composition"></el-input>
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
import {right} from "core-js/internals/array-reduce";

export default {
  name: "DishManage",
  data() {
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name: '',
      centerDialogVisible: false,
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      form: {
        id: '',
        name: '',
        category: '',
        dishcondition: '0',
        piece: '',
        composition: '',
      },
      cusColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 }
      ],
      rules: {
        name: [
          { required: true, message: '请输入菜名', trigger: 'blur' },
        ],
        category: [
          { required: true, message: '请选择菜品类别', trigger: 'blur' },
        ],
        dishcondition: [
          { required: true, message: '请选择菜品状态', trigger: 'blur' },
        ],
        piece: [
          { required: true, message: '请输入菜品价格', trigger: 'blur' },
        ],
        composition: [
          { required: true, message: '请输入原料组成', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    right,
    filterTag(value, row) {
      return row.dishcondition === value;
    },
    resetForm() {
      this.$refs.form.resetFields();
    },
    handleRemove(file) {
      console.log(file);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleDownload(file) {
      console.log(file);
    },
    mod(row) { // 编辑
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        // 赋值到表单
        this.form.id = row.id;
        this.form.name = row.name;
        this.form.category = row.category;
        this.form.piece = row.piece;
        this.form.dishcondition = row.dishcondition;
        this.form.composition = row.composition;
      });
    },
    del(id) { // 删除
      console.log(id);
      this.$axios.get(this.$httpUrl + '/dish/del?id=' + id).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl + '/dish/save', this.form).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl + '/dish/update', this.form).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl + '/dish/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.name,
        }
      }).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
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
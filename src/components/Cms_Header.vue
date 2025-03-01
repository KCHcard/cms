<script setup>

</script>

<template>
  <div style="display: flex; line-height: 80px; font-size: 25px">
    <div style="display: flex; align-items: center; justify-content: center;">
      <i :class="icon" @click="collapse" style="cursor: pointer"></i>
    </div>
    <div
        style="flex: 1; text-align: center; font-size: 30px; font-weight: bold; font-family: 'HarmonyOS Sans SC Black';">
      <span>餐饮管理系统</span>
    </div>
    <div style="font-size: 15px; font-family: 'HarmonyOS Sans SC Black';">
      <span>{{ user.username }}</span>
    </div>
    <el-dropdown trigger="click">
      <i class="el-icon-arrow-down" style="margin-left: 10px; font-size: 15px; margin-top: 10px; cursor: pointer"></i>
      <el-dropdown-menu slot="dropdown" style="margin-top: 5px;">
        <el-dropdown-item @click.native="toUser" style="font-size: 5px; margin-bottom: 5px">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="toSetting" style="font-size: 5px; margin-bottom: 5px">设置</el-dropdown-item>
        <el-dropdown-item @click.native="logout" style="font-size: 5px">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Cms_Header",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem("CurUser")),
    }
  },
  props: {
    icon: String,
  },
  methods: {
    toUser() {
      console.log('to_user');
      this.$router.push('/Home')

    },
    toSetting() {
      console.log('to_setting');
    },
    logout() {
      console.log('logout')

      this.$confirm('您确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',  //确认按钮的文字显示
        type: 'warning',
        center: true, //文字居中显示
      })
          .then(() => {
            this.$message({
              type: 'success',
              message: '退出登录成功'
            })

            this.$router.push('/')    //返回登录界面
            sessionStorage.clear();   //清空数据
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消退出登录'
            })
          })
    },
    collapse() {
      this.$emit('doCollapse');
    }
  },
  created() {
    this.$router.push('/Home')
  }
}
</script>

<style scoped>

</style>
<script setup>

</script>

<template>
  <el-menu
          default-active="/home"
          :collapse="isCollapse"
          :collapse-transition="false"
          style="height: 100vh;"
          router
  >
    <div style="margin-top: 200px"></div>
    <el-menu-item :index="'/'+item.menuclick" v-for="(item, i) in menu" :key="i">
      <i :class="item.menuicon" style="margin-right: 15px; font-size: 20px"></i>
      <span slot="title">{{item.menuname}}</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "Cms_Aside",
  data() {
    return {
    }
  },

  computed: {
    "menu":{
      get(){
        return this.$store.state.menu;
      }
    }
  },

  props: {
    isCollapse:Boolean
  }
}
</script>

<style scoped>

</style>
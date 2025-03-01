<script setup>


</script>

<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside :width="aside_width" style="background-color: rgb(238, 241, 246);height: 100%">
        <Cms_Aside :isCollapse="isCollapse"></Cms_Aside>
    </el-aside>

    <el-container style="height: 100%;">
      <el-header style="text-align: right; font-size: 12px;height: 100%; border-bottom: 1px solid #eee">
        <Cms_Header @doCollapse="doCollapse" :icon="icon"></Cms_Header>
      </el-header>

      <el-main style="height: 100%">
<!--        <Cms_Main></Cms_Main>-->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Cms_Aside from "@/components/Cms_Aside.vue";
import Cms_Header from "@/components/Cms_Header.vue";


export default {
  name: "IndexPage",
  components: {Cms_Aside,Cms_Header},
  data(){
    return {
      isCollapse:false,
      aside_width: '200px',
      icon:'el-icon-s-fold'
    }
  },
  methods: {
    doCollapse() {
      console.log(1111)

      this.isCollapse= !this.isCollapse;
      if(!this.isCollapse){   //展开时
        this.aside_width='200px'
        this.icon='el-icon-s-fold'
      }else{                 //收起
        this.aside_width ='64px'
        this.icon='el-icon-s-unfold'
      }
    }
  }
}
</script>

<style scoped>
.el-header {
  background-color: #ffffff;
  color: #333;
  line-height: 60px;
}
.el-main {
  background-color: #F4F7FC;
  padding: 30px;
}

.el-aside {
  color: #333;
}
</style>
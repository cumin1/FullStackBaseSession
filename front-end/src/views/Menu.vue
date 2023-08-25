<script >
export default {

  methods:{
    toSignin(){
      this.$router.push('/sign')
    }
  }
}

</script>

<template>
  <div class="menu">
    <el-menu style="border-radius: 12px;
  background-color: rgba(255, 255, 255, .2);
  backdrop-filter: blur(10px);
  box-shadow: 0 0 10px #333;"
             :default-active="activeIndex"
             class="el-menu-demo"
             mode="horizontal"
             :ellipsis="false"
             @select="handleSelect"
    >
      <el-menu-item index="0" style="margin-top: 2px">
        <el-avatar style="margin-top: 2px" :size="50" src="http://pic.imeitou.com/uploads/allimg/230825/10-230R51F441.png" />
      </el-menu-item>
      <div class="flex-grow"/>
      <el-menu-item index="1" @click="toSignin">注册账号</el-menu-item>
      <el-sub-menu index="2">
        <template #title>关于我们</template>
        <el-menu-item index="2-1">item one</el-menu-item>
        <el-menu-item index="2-2">item two</el-menu-item>
        <el-menu-item index="2-3">item three</el-menu-item>
        <el-sub-menu index="2-4">
          <template #title>item four</template>
          <el-menu-item index="2-4-1">item one</el-menu-item>
          <el-menu-item index="2-4-2">item two</el-menu-item>
          <el-menu-item index="2-4-3">item three</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
    </el-menu>
  </div>
</template>


<style scoped>
.menu {
  position: absolute;
  top: 0;
  height: 50px;
  width: 100%;
  font-size: 50px;

}

.flex-grow {
  flex-grow: 1;
}
</style>
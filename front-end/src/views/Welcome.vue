<script setup>

import Menu from "@/views/Menu.vue";
</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;">
    <Menu/>
    <el-image style="height: 100%;width: 100%;z-index: -1" src="https://img1.imgtp.com/2023/08/25/Gvi6nwJf.jpg" fit="cover"/>

    <router-view v-slot="{Component}">
      <transition name="el-zoom-in-top">
        <component :is="Component"/>
      </transition>
    </router-view>

  </div>
</template>

<style scoped>


</style>
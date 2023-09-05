<script setup>
import router from "@/router";
import {get} from "@/net/index"
import {ElMessage} from "element-plus";
import Kuangjia from "@/components/index/kuangjia.vue";

const logout = () => {
  get("/api/user/logout",(message)=>{
    ElMessage.success(message)
    router.push("/")
  })
  console.log("退出登录")
}
</script>

<template>
  <div>
    <kuangjia></kuangjia>
    <el-button @click="logout" type="danger">退出登录</el-button>
  </div>
</template>

<style scoped>

</style>
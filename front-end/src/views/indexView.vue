<script setup>
import router from "@/router";
import {get} from "@/net/index"
import {ElMessage} from "element-plus";

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
    欢迎进入到我的网站
    <el-button @click="logout" type="danger">退出登录</el-button>
  </div>
</template>

<style scoped>

</style>
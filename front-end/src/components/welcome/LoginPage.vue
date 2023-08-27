<script setup>
import {ElMessage} from "element-plus";
import {reactive} from "vue";
import {post,get} from "@/net/index"
import {User,Lock} from "@element-plus/icons-vue";
import router from "@/router";

const form = reactive({
  username:'',
  password:'',
  remember:false
})

const login = ()=>{
  if(!form.username || !form.password){
    ElMessage.warning("请填写用户名和密码！")
  }else{
    post('/api/user/login',{
      username:form.username,
      password:form.password,
      remember:form.remember
    },(message) =>{
      ElMessage.success(message)
      console.log("登录成功")
      router.push({path:'/index'})
    })
  }
}
</script>

<template>
  <div class="login">
    <div style="text-align: center;padding-top: 20px;">
      <el-text class="mx-1" size="large" style="margin-top: 10px;font-size: 25px ;color: #222222 ">登录</el-text>
      <div style="font-size: 14px;margin-top: 10px;color: grey">在进入系统之前，请先输入用户名和密码进行登录</div>
      <el-divider/>
      <el-input
          :prefix-icon="User"
          v-model="form.username"
          style="margin: 20px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="请输入用户名/邮箱"
      ></el-input>
      <el-input
          type="password"
          :prefix-icon="Lock"
          v-model="form.password"
          style="margin: 10px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="请输入密码"
      ></el-input>
    </div>
    <div style="margin-top: 10px">
      <el-row>
        <el-col :span="12" style="text-align: left;margin-left: 40px">
          <el-checkbox v-model="form.remember" label="记住我" size="large"/>
        </el-col>
      </el-row>
    </div>
    <div style="width: 100%;display: flex;margin-top: 30px">
      <el-button @click="login"
          style="width: 80%;height:50px;margin: 0 auto;opacity: 0.6;display: block" type="success" plain>登录</el-button>
    </div>

    <el-link style="position: absolute;bottom: 10px;left: 175px" class="mx-1">忘记密码？</el-link>

  </div>
</template>

<style scoped>
.login {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -250px;
  margin-left: -200px;
  width: 400px;
  height: 500px;
  font-size: 30px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, .2);
  backdrop-filter: blur(10px);
  box-shadow: 0 0 10px #333;

}
</style>
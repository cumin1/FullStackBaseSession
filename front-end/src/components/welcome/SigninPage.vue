<script >
import {User,Lock} from "@element-plus/icons-vue";
import router from "@/router";


export default{
  computed: {
    User() {
      return User
    }
  },
  methods:{
    toLogin(){
      router.push({path:'/'});
    }
  }
}
</script>

<template>
  <div class="signin">
    <div style="text-align: center;padding-top: 20px;">
      <el-text class="mx-1" size="large" style="margin-top: 10px;font-size: 25px ;color: #222222 ">注册</el-text>
      <div style="font-size: 14px;margin-top: 10px;color: grey">在进入系统之前，请先输入用户名和密码进行登录</div>
      <el-divider/>
      <el-input
          :prefix-icon="User"
          style="margin: 20px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="请输入用户名/邮箱"
      ></el-input>
      <el-input
          :prefix-icon="Lock"
          style="margin: 10px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="请输入密码"
      ></el-input>
    </div>

    <div style="width: 100%;display: flex;margin-top: 30px">
      <el-button style="width: 80%;height:50px;margin: 0 auto;opacity: 0.6;display: block" type="success" plain>注册
      </el-button>
    </div>
    <div style="width: 100%;display: flex;margin-top: 30px">
      <el-button @click="toLogin" style="width: 80%;height:50px;margin: 0 auto;opacity: 0.6;display: block" type="warning" plain>返回登录
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.signin {
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
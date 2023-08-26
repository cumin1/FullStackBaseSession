<script setup>
import {Back, Promotion, Search, Unlock, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive} from "vue";

const form = reactive({
  username: '',
  password: '',
  repeatpassword: '',
  email:'',
  emailverify:''
})


const toLogin = () => {
  router.push({path: '/'});
}

</script>

<template>
  <div class="signin">
    <div style="font-size: 20px;position: absolute;left: 15px;top: 15px;">
      <el-button @click="toLogin" style="opacity: 0.5;border-radius: 50px;" type="success" plain :icon="Back"></el-button>
    </div>
    <div style="text-align: center;padding-top: 20px;">
      <el-text class="mx-1" size="large" style="margin-top: 10px;font-size: 25px ;color: #222222 ">注册</el-text>
      <div style="font-size: 14px;margin-top: 10px;color: grey">请设置您的用户名和密码</div>
      <el-divider/>
      <el-input
          v-model="form.username"
          :prefix-icon="User"
          style="margin: 5px 15px 10px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="用户名/邮箱"
      ></el-input>
      <el-input
          v-model="form.password"
          :prefix-icon="Unlock"
          style="margin: 10px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="密码"
      ></el-input>
      <el-input
          v-model="form.repeatpassword"
          :prefix-icon="Unlock"
          style="margin: 10px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          placeholder="重复密码"
      ></el-input>
      <el-input
          v-model="form.email"
          :prefix-icon="Promotion"
          style="margin: 10px 15px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
          class="w-50 m-2"
          size="large"
          type="email"
          placeholder="电子邮件地址"
      ></el-input>
      <div style="">
        <el-input
            v-model="form.emailverify"
            :prefix-icon="Unlock"
            style="margin: 10px 15px;height:50px;width: 60%;background: blue;opacity: 0.6;border-radius:4px"
            class="w-50 m-2"
            size="large"
            type="email"
            placeholder="请输入电子邮件验证码"
        ></el-input>
        <el-button style="width: 20%;height: 40px;background-color: rgba(255, 255, 255, .2);
  backdrop-filter: blur(10px);">获取验证码</el-button>
      </div>

    </div>

    <div style="width: 100%;display: flex;margin-top: 30px">
      <el-button style="width: 80%;height:50px;margin: 0 auto;opacity: 0.6;display: block" type="success" plain>注册
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
  height: 600px;
  font-size: 30px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, .2);
  backdrop-filter: blur(10px);
  box-shadow: 0 0 10px #333;

}
</style>
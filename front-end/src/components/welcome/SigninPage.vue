<script setup>
import {Back, Promotion, Search, Unlock, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net";


const form = reactive({
  username: '',
  password: '',
  repeatpassword: '',
  email: '',
  emailverify: '',
})

const validatePass = (rule, value, callback) => { //^[a-zA-Z0-9\u4e00-\u9fa5]+$
  if (value === '') {
    callback(new Error('用户名不能为空'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能包含中文和英文'))
  } else {
    callback()
  }
}

const passwordvalidate = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else if (value !== form.password) {
    callback(new Error('输入密码不一致'))
  } else {
    callback()
  }
}

const toLogin = () => {
  router.push({path: '/'});
}

const rules = {
  username: [
    {validator: validatePass, trigger: ['blur', 'change']},
    {min: 2, max: 8, message: '用户名长度必须在2-8个之间', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: ['blur', 'change']},
    {min: 6, max: 16, message: '密码长度必须在6-16个之间', trigger: 'blur'}
  ],
  repeatpassword: [
    {validator: passwordvalidate, trigger: ['blur', 'change']},
    {min: 6, max: 16, message: '密码长度必须在6-16个之间', trigger: 'blur'}
  ],
  email: [
    {required: true, message: '请输入邮件地址', trigger: 'blur',},
    {type: 'email', message: '请输入正确的邮件地址', trigger: ['blur', 'change'],},
  ],
  emailverify:[
    {required: true, message: '请输入获取的验证码', trigger: 'blur',},
  ]
}

const formRef = ref()
const isEmailValid = ref(false)
const isSendEmail = ref(false)
const coldTime = ref(0)
const onValidate = (prop,isValid,_)=>{
  if(prop === 'email'){
    isEmailValid.value = isValid
  }
}

const register = ()=>{
  formRef.value.validate((isValid)=>{
    console.log(isValid)
    if(isValid){
      post("/api/user/register",{
            username:form.username,
            password:form.password,
            email:form.email,
            code:form.emailverify
      },(message)=>{
        ElMessage.success(message)
        router.push({path:'/'})
          })
    }else {
      ElMessage.warning("请完整填写上述注册表单内容")
    }
  })
}

// 获取验证码
const getEmailCode = ()=>{
  post('/api/user/email',{email:form.email},(message)=>{
    ElMessage.success(message)
    coldTime.value = 60
    setInterval(()=>coldTime.value-- , 1000)
  })

}
</script>

<template>
  <div class="signin">
    <div style="font-size: 20px;position: absolute;left: 15px;top: 15px;">
      <el-button @click="toLogin" style="opacity: 0.5;border-radius: 50px;" type="success" plain
                 :icon="Back"></el-button>
    </div>
    <div style="text-align: center;padding-top: 20px;">
      <el-text class="mx-1" size="large" style="margin-top: 10px;font-size: 25px ;color: #222222 ">注册</el-text>
      <div style="font-size: 14px;margin-top: 10px;color: grey">请设置您的用户名和密码</div>
      <el-divider/>

      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <el-input
              :maxlength="8"
              v-model="form.username"
              :prefix-icon="User"
              style="margin-left: 35px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
              class="w-50 m-2"
              size="large"
              placeholder="用户名/邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              :maxlength="16"
              v-model="form.password"
              :prefix-icon="Unlock"
              style="margin-left: 35px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
              class="w-50 m-2"
              size="large"
              placeholder="密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="repeatpassword">
          <el-input
              v-model="form.repeatpassword"
              :prefix-icon="Unlock"
              style="margin-left: 35px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
              class="w-50 m-2"
              size="large"
              placeholder="重复密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input
              v-model="form.email"
              :prefix-icon="Promotion"
              style="margin-left: 35px;height:50px;width: 80%;background: blue;opacity: 0.6;border-radius:4px"
              class="w-50 m-2"
              size="large"
              type="email"
              placeholder="电子邮件地址"
          ></el-input>
        </el-form-item>
        <el-form-item prop="emailverify">
          <el-input
              :maxlength="6"
              v-model="form.emailverify"
              :prefix-icon="Unlock"
              style="margin-left: 35px;height:50px;width: 60%;background: blue;opacity: 0.6;border-radius:4px"
              class="w-50 m-2"
              size="large"
              type="email"
              placeholder="请输入验证码"
          ></el-input>
          <el-button @click="getEmailCode" style="margin-left: 10px;width: 20%;height: 40px;background-color: rgba(255, 255, 255, .2);
  backdrop-filter: blur(10px);" :disabled="!isEmailValid || coldTime > 0">
            {{ coldTime> 0 ? "请稍后"+coldTime+"秒" :  "获取验证码"}}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div style="width: 100%;margin-top: 30px">
      <el-button  @click="register" style="width: 80%;height:50px;margin: 0 auto;opacity: 0.6;display: block" type="success" plain>注册
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

/deep/ .el-form-item__error{
  margin-left: 35px;
}
</style>
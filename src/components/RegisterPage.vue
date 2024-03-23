<template>
  <div class="login-page-container">
    <a-form class="login-page-form"
    @finish="handleSubmit"
    :model="formState">
      <legend class="login-page-legend">注册账户</legend>
      <a-form-item
        class="login-page-input"
        label="用户名"
        name="username"
        :rules="[{required: true,message: '请输入用户名!'}]">
        <a-input id="username"
        autocomplete="off"
        placeholder="请输入用户名"
        v-model:value="formState.username"></a-input>
      </a-form-item>
      <a-form-item
        class="login-page-input" 
        label="密码"
        name="password"
        :rules="[{required: true,message: '请输入密码!'}]">
        <a-input-password id="password"
        autocomplete="off"
        placeholder="请输入密码"
        v-model:value="formState.password"></a-input-password>
      </a-form-item>
      <a-form-item
        class="login-page-input" 
        label="确认密码"
        name="confirmPassword"
        :rules="[{validator: validatePass2,trigger: 'change'}]">
        <a-input-password id="confirmPassword"
        autocomplete="off"
        placeholder="请再次输入密码"
        v-model:value="formState.confirmPassword"></a-input-password>
      </a-form-item>
      <div class="login-page-btnbox">
        <a-button class="login-page-btn" 
        type="primary"  
        html-type="submit"
        :loading="loading">注册</a-button>
        <router-link to="/login" class="login-page-tag">已有账号?立即登录</router-link> 
      </div>
    </a-form>
  </div>
</template>
<script>

export default {
  data() {
    return {
      formState: {
        username: '',
        password: '',
        confirmPassword: '',
      },
      formValidate: {
        username: false,
        password: false,
        confirmPassword: false,
      },
      loading: false,
    }
  },
  methods: {
    handleSubmit(event) {
      const baseURL = this.$store.state.baseURL;
      const url = baseURL + "/register";
      const body = {
        username: this.formState.username,
        password: this.formState.password
      };
      this.loading = true;
      fetch(url,{
        method: 'post',
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body)
      }).then(response => response.json())
      .then(response => {
        if(response.code === 200) {
          console.log(response);
          this.$router.push({path: '/login'});
          this.$message.success(response.message);
        } else {
          console.log(response);
          this.$message.warning(response.message);
        }
        this.loading = false;
      })
      .catch(error => {
        console.error(error);
        this.$message.error('网络连接异常');
        this.loading = false;
      });
    },
    async validatePass2(_rule,value) {
      if (value === '') {
        return Promise.reject('请再次输入密码');
      } else if (value !== this.formState.password){
        return Promise.reject('两次密码输入不一致');
      } else {
        return Promise.resolve();
      }
    }
  }
}
</script>
<style scoped>
.login-page-input {
  font-size: 1.1rem;
  width: 80%;
  margin-left: auto;
  margin-right: auto;
}
.login-page-tag {
  position: absolute;
  right: 10%;
  font-size: small;
}
.login-page-btn {
  width: 80px;
  height: 30px;
}
.login-page-btnbox {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}
.login-page-legend {
  text-align: center;
  margin-top: 40px;
  margin-bottom: 40px;
}
.login-page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.login-page-form {
  width: 400px;
  height: 320px;
}
</style>
<template>
  <div class="login-page-container">
    <form class="login-page-form" action="#"
    @submit.prevent="handleSubmit">
      <legend class="login-page-legend">注册账户</legend>
      <label class="login-page-label">用户名：
        <input id="username"
        class="login-page-input" type="text" autocomplete="off"
        name="username" placeholder="请输入用户名"
        v-model="username" required/>
      </label>
      <label class="login-page-label">密码：
        <input id="password"
        class="login-page-input" autocomplete="off"
        type="password" name="password" placeholder="请输入密码" 
        v-model="password" required/>
      </label>
      <label class="login-page-label">确认密码：
        <input id="confirmPassword"
        class="login-page-input" autocomplete="off"
        type="password" placeholder="请再次输入密码" 
        v-model="confirmPassword" required/>
      </label>
      <div class="login-page-btnbox">
        <button class="login-page-btn" type="submit">注册</button>
        <router-link to="/login" class="login-page-tag">已有账号?立即登录</router-link> 
      </div>
    </form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: '',
    }
  },
  methods: {
    handleSubmit(event) {
      const url = "http://localhost:8080/register";
      const body = {
        username: this.username,
        password: this.password
      };
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
        } else {
          console.log(response);
        }
      })
      .catch(error => console.error(error));
    }
  }
}
</script>
<style scoped>
.login-page-input {
  font-size: 1.1rem;
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
.login-page-btn:hover {
  background-color: grey;
  color: white;
}
.login-page-label {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.8rem;
  margin-bottom: 20px;
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
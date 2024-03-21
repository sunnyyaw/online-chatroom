<template>
  <div class="login-page-container">
    <form @submit.prevent="handleSubmit"
    class="login-page-form">
      <legend class="login-page-legend">用户名密码登录</legend>
      <label class="login-page-label">用户名：
        <input class="login-page-input" type="text" 
        name="username" placeholder="请输入用户名" autocomplete="off"
        v-model="username" required/>
      </label>
      <label class="login-page-label">密码：
        <input class="login-page-input"  autocomplete="off"
        type="password" name="password" placeholder="请输入密码" 
        v-model="password" required/>
      </label>
      <div class="login-page-btnbox">
        <button class="login-page-btn" type="submit">登录</button>
        <router-link to="/register" class="login-page-tag">没有账户?立即注册</router-link> 
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
    }
  },
  methods: {
    handleSubmit(event) {
      const baseURL = this.$store.state.baseURL;
      const url = baseURL + "/login";
      const username = this.username;
      const password = this.password;
      const body = {
        username: username,
        password: password
      }
      fetch(url,{
        method: 'post',
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
      }).then(response => response.json())
      .then(response => {
        if (response.code === 200) {
          const token = response.data;
          this.$store.commit('storeToken',token);
          this.$store.commit('storeUsername',username);
          this.$store.commit('loadMessage');
          this.$router.push('/');
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
  margin-bottom: 40px;
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
  height: 300px;
}
</style>
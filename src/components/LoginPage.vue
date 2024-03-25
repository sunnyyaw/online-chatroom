<template>
  <div class="login-page-container">
    <a-form 
    @finish="handleSubmit"
    :model="formState"
    class="login-page-form">
      <legend class="login-page-legend">用户名密码登录</legend>
      <a-form-item
        label="用户名"
        name="username"
        class="login-page-input" 
        :rules="[{required: true,message: '请输入用户名!'}]"
      >
        <a-input v-model:value="formState.username" 
        placeholder="请输入用户名" autocomplete="off">
        </a-input>
      </a-form-item>

      <a-form-item
        label="密码"
        name="password"
        class="login-page-input"
        :rules="[{required: true,message: '请输入密码!'}]"
      >
        <a-input-password v-model:value="formState.password" 
        placeholder="请输入密码" autocomplete="off">
        </a-input-password>
      </a-form-item>

      <div class="login-page-btnbox">
        <a-button class="login-page-btn"
         type="primary"
         html-type="submit"
         :loading="loading">
          登录
        </a-button>
        <router-link to="/register" class="login-page-tag">没有账户?立即注册</router-link> 
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
      },
      loading: false,
    }
  },
  methods: {
    handleSubmit(event) {
      const username = this.formState.username;
      const password = this.formState.password;
      const body = {
        username: username,
        password: password
      };
      this.loading = true;
      fetch("/api/login",{
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
  height: 300px;
  border: 1px solid gray;
  border-radius: 10px;
  box-shadow: 2px 2px 8px 2px dimgray;
}
</style>
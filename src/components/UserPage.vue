<template>
  <div class="page">
    <div
    class="container">
    <a-page-header
    style="font-size: small;"
    title="返回聊天室"
    @back="handleBack" />
    <a-descriptions 
    title="用户信息"
    bordered>
      <a-descriptions-item label="头像">
        <a-avatar>
          <template #icon>
            <UserOutlined />
          </template>
        </a-avatar>
      </a-descriptions-item>
      <a-descriptions-item label="用户名">{{$store.state.username}}</a-descriptions-item>
      <a-descriptions-item label="密码"></a-descriptions-item>
    </a-descriptions>
    <div class="userpage-btn-group">
      <a-button type="primary">编辑</a-button>
      <a-button type="primary">保存</a-button>
    </div>
    </div>
  </div>
</template>
<script>
import {UserOutlined } from '@ant-design/icons-vue';
export default {
  data() {
    return {

    }
  },
  components: {
    UserOutlined
  },
  methods: {
    handleBack() {
      this.$router.push('/');
    }
  },
}
</script>
<style scoped>
.userpage-btn-group {
  display: flex;
  margin: 10px auto;
  gap: 20px;
  justify-content: center;
}
.page {
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: center;
  align-items: center;
}
.container {
  width: 40%;
  height: 80%;
}
</style>
<template>
  <div class="page">
    <div class="container">
      <a-page-header style="font-size: small;" title="返回聊天室" @back="handleBack" />
      <a-descriptions title="用户信息" bordered>
        <a-descriptions-item label="头像">
          <a-upload name="avatar" v-model:file-list="filelist"
            list-type="picture-card" class="avatar-uploader" :show-upload-list="false"
            action="http://localhost:8080/user/avatar" :before-upload="beforeUpload" @change="handleChange"
            :headers="requestHeaders"
            :multiple="false">
            <a-avatar v-if="imgUrl" class="userpage-img">
              <template #icon>
                <img :src="imgUrl" alt="avatar" class="userpage-img"/>
              </template>
            </a-avatar>
            <div v-else>
              <LoadingOutlined v-if="loading"></LoadingOutlined>
              <PlusOutlined v-else></PlusOutlined>
              <div class="ant-upload-text">上传图片</div>
            </div>
          </a-upload>
        </a-descriptions-item>
        <a-descriptions-item label="用户名">{{ $store.state.username }}</a-descriptions-item>
        <a-descriptions-item label="密码"></a-descriptions-item>
      </a-descriptions>
      <div class="userpage-btn-group">
        <a-button type="primary">编辑</a-button>
        <a-button type="primary">保存</a-button>
      </div>
    </div>
  </div>
</template>
<script >
import { PlusOutlined, LoadingOutlined, UserOutlined } from '@ant-design/icons-vue';
export default {
  data() {
    return {
      imgUrl: null,
      loading: false,
      filelist: [],
      requestHeaders: {
        token: '',
      }
    }
  },
  components: {
    UserOutlined, PlusOutlined, LoadingOutlined
  },
  mounted() {
    fetch(this.$store.state.baseURL + `/user/${this.$store.state.username}`, {
        headers: {
          'token': this.$store.state.token
        }
      })
      .then(response => response.json())
      .then(response => {
        if (response.code === 200) {
          console.log(response);
          this.imgUrl = response.data.avatar ? 
          `http://localhost:8080/images/${response.data.avatar}?d=${new Date()}` :
          null;
        } else {
          console.log(response);
        }
      })
      .catch(error => console.error(error));
    this.requestHeaders.token = this.$store.state.token;
  },
  methods: {
    handleBack() {
      this.$router.push('/');
    },
    beforeUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
      if (!isJpgOrPng) {
        this.$message.error('只能上传JPG或PNG格式图片');
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('图片必须小于2M');
      }
      return isJpgOrPng && isLt2M;
    },
    handleChange(info) {
      if (info.file.status === 'uploading') {
        this.loading = true;
        return;
      }
      if (info.file.status === 'done') {
        this.loading = false;
        this.imgUrl = info.file.response.data + `?d=${new Date()}`;
        return;
      }
      if (info.file.status === 'error') {
        this.loading = false;
        this.$message.error('上传失败');
      }
    }
  },
}
</script>
<style scoped>
.userpage-img {
  object-fit: contain;
  width: 100%;
  height: 100%;
}
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
  height: 80%;
  border: 1px solid gray;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 2px 2px 8px 2px dimgray;
}
</style>
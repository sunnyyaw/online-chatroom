<template>
  <div class="textpad-container">
    <a-textarea class="textpad-textarea" placeholder="输入想要发送的内容" autocomplete="off" 
    v-model:value="text" :maxlength="355"/>
    <a-button type="primary" class="textpad-btn" @click="handleSend">发送</a-button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      text: '',
    }
  },
  methods: {
    handleSend() {
      const socket = this.$store.state.socket;
      const chater = this.$store.state.chater;
      if(socket) {
        if (chater === '公共聊天室') {
          const msg = 'G/' + chater + '#' + this.text;
          socket.send(msg);
          console.log(`Send message ${msg}`);
        } else {
          const msg = 'M/' + chater + '#' + this.text;
          socket.send(msg);
          console.log(`Send message ${msg}`);
        }
        this.text = '';
      }
    }
  }
}
</script>
<style scoped>
.textpad-container {
  display: flex;
  flex-direction: column;
  align-items: end;
}
.textpad-btn {
  position: absolute;
  right: 5%;
  bottom: 2%;
  width: 80px;
}
.textpad-textarea {
  resize: none;
  height: 100%;
  width: 100%;
}
</style>
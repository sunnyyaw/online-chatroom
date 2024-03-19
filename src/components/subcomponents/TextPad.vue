<template>
  <div class="textpad-container">
    <textarea class="textpad-textarea" autocomplete="off" v-model="text" />
    <div class="textpad-bottom">
      <button class="textpad-btn" @click="handleSend">发送</button>
    </div>
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
      if(socket) {
        socket.send(this.text);
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
}
.textpad-bottom {
  position: relative;
  flex-grow: 1;
  display: flex;
  align-items: center;
}
.textpad-btn {
  position: absolute;
  right: 5%;
}
.textpad-btn:hover {
  background-color: grey;
  color: white;
}
.textpad-textarea {
  resize: none;
  height: 80%;
  width: 100%;
}
</style>
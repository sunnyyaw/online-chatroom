<template>
  <div>
    <div class="friend-bar-user">当前用户{{ username }}</div>
    <div>
      <div class="friend-bar-head">好友列表</div>
      <div class="friend-bar-item" @click="handleClick('公共聊天室')">
        <span>公共聊天室</span>
      </div>
      <div class="friend-bar-item" v-for="friend in Object.keys(friends)"
      @click="handleClick(friend)">
        <span>{{ friend }} {{ friends[friend] ? '在线' : '离线' }}</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        username: '',
        friends: {},
      }
    },
    mounted() {
      this.username = localStorage.getItem('username');
    },
    methods: {
      init() {
        const socket = this.$store.state.socket;
        const friendStr = localStorage.getItem(`${this.username}friends`);
        if (!friendStr) {
          this.friends = {};
        } else {
          this.friends = JSON.parse(friendStr);
        }
        for(let key of Object.keys(this.friends)) {
          this.friends[key] = false;
        }
        socket.addEventListener('message',(event) => {
          const message = event.data;
          if (message.startsWith('L/')) {
            const loginUser = message.split('/')[1];
            if (loginUser === this.username) {
              return;
            }
            this.friends[loginUser] = true;
            localStorage.setItem(`${this.username}friends`,JSON.stringify(this.friends));
          } else if (message.startsWith('E/')) {
            const exitUser = message.split('/')[1];
            this.friends[exitUser] = false;
          }
        });
      },
      handleClick(friend) {
        this.$store.commit('changeChater',friend);
      }
    }
  }
</script>
<style scoped>
.friend-bar-item {
  user-select: none;
}
.friend-bar-item:hover {
  background-color: gray;
  color: white;
}
.friend-bar-head {
  text-align: center;
}
</style>
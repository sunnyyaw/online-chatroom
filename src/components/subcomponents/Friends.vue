<template>
  <div>
    <div class="friend-bar-user">当前用户{{ username }}</div>
    <div>

      <a-button type="primary" class="friend-bar-btn" @click="() => setModalVisible(true)">
        添加好友
      </a-button>
      <a-modal :open="modalVisible" title="搜索好友"
      @ok="() => setModalVisible(false)"
      @cancel="() => setModalVisible(false)">
        <a-input-search placeholder="请输入要添加的好友名"
        aria-placeholder="请输入要添加的好友名"
        @search="handleSearch"
        ></a-input-search>
        <a-list item-layout="horizonal" :data-source="searchResult">
          <div class="friend-list-item" v-for="item in searchResult">
            <span>{{ item.username }}</span>
            <a-button type="primary" class="friend-list-btn" 
            @click="handleAddFriend(item.username)">添加</a-button>
          </div>
        </a-list>
      </a-modal>

      <div class="friend-bar-head">好友列表</div>
      <div class="friend-bar-item" @click="handleClick('公共聊天室')">
        <span>公共聊天室</span>
      </div>
      <div class="friend-bar-item" v-for="friend in Object.keys(friends)"
      @click="handleClick(friend)">
        <span v-if="friends[friend].isFriend">{{ friend }} {{ friends[friend].online ? '在线' : '离线' }}</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        username: '',
        modalVisible: false,
        friends: {},
        searchResult: [],
      }
    },
    components: {
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
          this.friends[key].online = false;
        }
        socket.addEventListener('message',(event) => {
          const message = event.data;
          if (message.startsWith('L/')) {
            const loginUser = message.split('/')[1];
            if (loginUser === this.username) {
              return;
            }
            if (this.friends[loginUser] !== undefined) {
              this.friends[loginUser].online = true;
            }
          } else if (message.startsWith('E/')) {
            const exitUser = message.split('/')[1];
            this.friends[exitUser].online = false;
          }
        });
      },
      handleClick(friend) {
        this.$store.commit('changeChater',friend);
      },
      setModalVisible(value) {
        this.modalVisible = value;
      },
      handleSearch(value) {
        const baseURL = this.$store.state.baseURL;
        const token = localStorage.getItem('token');
        fetch(baseURL + `/user?username=${value}`,{
          headers: {
            'token': token
          }
        })
        .then(response => response.json())
        .then(response => {
          this.$message.success('查询成功');
          this.searchResult = response.data;
        })
        .catch(error => console.error());
      },
      handleAddFriend(friendName) {
        const socket = this.$store.state.socket;
        if (socket) {
          socket.send(`Q/${friendName}`);
        }
        this.friends[friendName] = {
          online: false,
          isFriend: true
        }
        localStorage.setItem(`${this.username}friends`,JSON.stringify(this.friends));
        this.modalVisible = false;
      }
    }
  }
</script>
<style scoped>
.friend-list-item {
  overflow: hidden;
}
.friend-list-btn {
  float: right;
}
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
.friend-bar-btn {
  display: block;
  width: 100%;
}
.friend-bar-btn:hover {
  background-color: gray;
  color: white;
}
</style>
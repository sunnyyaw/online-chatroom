<template>
  <div>
    <div class="friend-bar-user">
      <span>当前用户{{ username }}</span>
      <a-button class="friend-bar-logout"
      @click="handleLogout">
        <poweroff-outlined />注销
      </a-button>
    </div>
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
            <a-button v-show="item.username !== $store.state.username"
            type="primary" 
            class="friend-list-btn" 
            @click="handleAddFriend(item.username)">添加</a-button>
          </div>
        </a-list>
      </a-modal>

      <div class="friend-bar-head">好友列表</div>
      <div :class="$store.state.chater === '公共聊天室' ? 
      'friend-bar-item friend-bar-item-active' :
      'friend-bar-item'" 
      @click="handleClick('公共聊天室')">
        <span>公共聊天室
        </span>
        <a-badge v-if="$store.state.messages['公共聊天室']" 
        :count="$store.state.messages['公共聊天室'].unread">
        </a-badge>
      </div>
      <div :class="$store.state.chater === friend ? 
      'friend-bar-item friend-bar-item-active' :
      'friend-bar-item'" 
      v-for="friend in Object.keys(friends)"
      @click="handleClick(friend)">
        <span v-if="friends[friend].isFriend">
          {{ friend }} 
        </span>
        <a-badge :status="friends[friend].online ? 'success' : 'default'"
        :text="friends[friend].online ? '在线' : '离线'">
        </a-badge>
        <a-badge v-if="$store.state.messages[friend]" 
        :count="$store.state.messages[friend].unread">
        </a-badge>
      </div>
    </div>
  </div>
</template>
<script >
  import {PoweroffOutlined } from '@ant-design/icons-vue';
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
      PoweroffOutlined
    },
    mounted() {
      this.username = this.$store.state.username;
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
            if (this.friends[loginUser]) {
              this.friends[loginUser].online = true;
            }
          } else if (message.startsWith('E/')) {
            const exitUser = message.split('/')[1];
            if (this.friends[exitUser]) {
              this.friends[exitUser].online = false;
            }
          }
        });
      },
      handleClick(friend) {
        this.$store.commit('changeChater',friend);
        this.$store.commit('clearUnread',friend);
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
      },
      handleLogout(event) {
        this.$store.state.socket.close();
        this.$router.replace('/login');
        this.$store.commit('logout');
      }
    }
  }
</script>
<style scoped>
.friend-list-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.friend-list-btn {
  position: relative;
  right: 10px;
}
.friend-bar-user {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.friend-bar-item {
  user-select: none;
  font-size: 14px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
  margin: 2px 0;
  padding: 6px;
  border-radius: 6px;
  outline: none;
  background-color: white;
  transition: background-color 0.3s cubic-bezier(0.645,0.045,0.355,1) 0s;
}
.friend-bar-item:hover {
  background-color: rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: background-color 0.3s cubic-bezier(0.645,0.045,0.355,1) 0s;
}
.friend-bar-item-active {
  background-color: #e6f4ff;
  color: #1677ff;
  transition: background-color 0.3s cubic-bezier(0.645,0.045,0.355,1) 0s;
}
.friend-bar-head {
  text-align: center;
}
.friend-bar-btn {
  display: block;
  width: 100%;
}
</style>
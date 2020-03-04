<style scoped>
.layout {
  border: 1px solid #d7dde4;
  background: #f5f7f9;
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  min-width: 1000px;
}

.layout-logo {
  float: left;
}

.layout-search {
  height: 30px;
  border-radius: 3px;
  float: left;
  position: relative;
  left: 30px;
}

.layout-title {
  margin-left: 50px;
  height: inherit;
  float: left;
}

/* .layout-title .home-text { */
/* color: #36b7ab; */
/* font-weight: bold; */
/* } */

.layout-title .home-text:hover {
  color: #a5e5df;
}

.menu-layout {
  height: 65px;
  width: 100%;
}
.layout-nav {
  height: inherit;
  float: right;
}

.layout-footer-center {
  text-align: center;
  background: #fff;
}

.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}

.avatar-badge-wrapper {
  position: relative;
  float: right;
  cursor: pointer;
}

.avatar-badge-wrapper .msg-num {
  position: absolute;
  top: 9px;
  right: -12px;
  color: #fff;
  background-color: #36b7ab;
  border-radius: 50%;
  padding: 2px 5px;
  line-height: 1;
}

@keyframes ani-demo-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
<template>
  <div class="layout">
    <Layout>
      <Header style="position: absolute;width: 100%;background:#fff;padding:0 0;z-index: 1000;">
        <Menu
          mode="horizontal"
          theme="light"
          class="menu-layout"
          active-name="interest"
          @on-select="m=>{menuSelect(m)}"
        >
          <div style="width: 95%;margin: 0 auto">
            <div class="layout-logo">
              <a @click="backHome()">
                <img
                  src="../../images/logo.jpg"
                  style="width: 120px;height: 40px;"
                  align="absmiddle"
                />
              </a>
            </div>
            <div class="layout-title">
              <MenuItem name="interest">
                <span class="home-text">主页</span>
              </MenuItem>
              <MenuItem name="article">
                <span class="home-text">文章</span>
              </MenuItem>
            </div>
            <div class="layout-search">
              <Input
                v-model="searchValue"
                icon="android-search"
                placeholder="Enter something..."
                @on-enter="search()"
              />
            </div>
            <div v-if="loginFlag" class="layout-nav">
              <MenuItem name="1">{{user.name}}</MenuItem>
              <MenuItem name="7" v-if="!stateFlag">
                <Icon type="md-hand"></Icon>签到
              </MenuItem>
              <MenuItem name="8" v-if="stateFlag">
                <Icon type="md-hand"></Icon>已签到
              </MenuItem>
              <MenuItem name="2">
                <Icon type="ios-mail"></Icon>邮件
              </MenuItem>
              <Submenu name="3">
                <template slot="title">
                  <Icon type="md-list-box" />文章
                </template>
                <MenuItem name="31">
                  <Icon type="md-create" />写文章
                </MenuItem>
                <MenuItem name="32">
                  <Icon type="md-list" />我的文章
                </MenuItem>
              </Submenu>
              <MenuItem name="4">
                <Icon type="md-log-out"></Icon>退出
              </MenuItem>
              <MenuItem name="5" v-if="consoleFlag">
                <Icon type="md-settings"></Icon>控制台
              </MenuItem>
            </div>
            <div type="success" class="avatar-badge-wrapper" @click="toMessages">
              <img
                v-if="loginFlag"
                style="width: 30px;height: 30px; margin-top: 16px;border-radius: 100%;"
                :src="user.headimg"
              />

              <span v-if="unreadMsgCount > 0" class="msg-num">{{unreadMsgCount}}</span>
            </div>

            <div v-if="!loginFlag" class="layout-nav">
              <MenuItem name="6">
                <Icon type="md-log-in"></Icon>登录
              </MenuItem>
            </div>
          </div>
        </Menu>
      </Header>
      <Content :style="{margin: '80px 0 40px 0'}">
        <router-view></router-view>
      </Content>
      <Footer class="layout-footer-center">
        <div>
          <a href="https://github.com/leleqin" target="_blank">
            <!-- <Icon style="color: rebeccapurple;" size="40" type="logo-github"></Icon> -->
          </a>
        </div>
        <p>2019-2020 &copy;</p>
      </Footer>
    </Layout>
    <!-- 邮件modal -->
    <Modal
      :mask-closable="false"
      :visible.sync="emailModal"
      :loading="loading"
      v-model="emailModal"
      width="600"
      title="联系管理员"
      @on-ok="emailOk('email')"
      @on-cancel="cancel()"
    >
      <Form ref="email" :rules="emailRule" :model="email" :label-width="80">
        <FormItem label="标题" prop="title">
          <Input v-model="email.title" placeholder="请输入标题" />
        </FormItem>
        <FormItem label="email" prop="email">
          <Input v-model="email.email" placeholder="请输入email" />
        </FormItem>
        <FormItem label="姓名" prop="name">
          <Input v-model="email.name" placeholder="请输入姓名" />
        </FormItem>
        <FormItem label="内容" prop="content">
          <Input
            v-model="email.content"
            type="textarea"
            :autosize="{minRows: 2,maxRows: 5}"
            placeholder="Enter something..."
          />
        </FormItem>
      </Form>
    </Modal>
    <!-- 签到modal -->
    <Modal
      :mask-closable="false"
      :visible.sync="stateModal"
      :loading="loading"
      v-model="stateModal"
      width="300"
      title="签到"
      @on-ok="stateOk('state')"
      @on-cancel="cancel()"
    >
      <p slot="header" style="color:#f60;text-align:center">
        <Icon type="ios-information-circle"></Icon>
        <span>签到提示</span>
      </p>
      <div style="text-align:center">
        <p>是否确认签到？</p>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long :loading="modal_loading" @click="publish()">确认</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      loginFlag: false,
      stateFlag: false,
      consoleFlag: false,
      loading: true,
      searchValue: "",
      emailModal: false,
      stateModal: false,
      userId: 0,
      //用户未读消息个数
      unreadMsgCount: 0,
      email: {
        title: "",
        email: "",
        name: "",
        content: ""
      },
      user: {
        loginName: "",
        email: "",
        headimg: "",
        name: ""
      },
      emailRule: {
        title: [
          {
            type: "string",
            required: true,
            message: "请输入密码",
            trigger: "blur"
          }
        ],
        email: [
          { required: true, message: "输入邮箱", trigger: "blur" },
          { type: "email", message: "输入正确的邮箱格式", trigger: "blur" }
        ],
        passwordAgain: [
          {
            type: "string",
            required: true,
            message: "请输入再次输入密码",
            trigger: "blur"
          }
        ],
        name: [
          {
            type: "string",
            required: true,
            message: "请输入姓名",
            trigger: "blur"
          }
        ],
        content: [{ required: true, message: "请输入内容", trigger: "blur" }]
      }
    };
  },
  created() {
    var code = this.$route.query.code;
    var state = this.$route.query.state;
    if (this.$store.getters._isMobile) {
      if (code != null && code != "" && state != null && state != "") {
        this.$router.replace("/mobile" + "?code=" + code + "&state=" + state);
        return;
      } else {
        this.$router.replace("/mobile");
        return;
      }
    }
    this.login(code, state);
  },
  methods: {
    userGet() {
      let _this = this;
      this.axios({
        method: "get",
        url: "/public/user"
      })
        .then(
          function(response) {
            if (response.data.data != null && response.data.data != "") {
              this.loginFlag = true;
              this.userSet(response.data.data);
              this.userId = response.data.data.id;
              if (response.data.data.usertype != 0) {
                this.consoleFlag = true;
              }
              this.stateGet();
              return this.axios({
                method: "get",
                url: "/msgrecords/unreadnum"
              });
            } else {
              return Promise.resolve(0);
            }
          }.bind(this)
        )
        .then(function(response) {
          if (response === 0) {
            _this.unreadMsgCount = response;
          } else {
            _this.unreadMsgCount = response.data.data;
          }
        })
        .catch(
          function(error) {
            this.$Message.error("无权限");
          }.bind(this)
        );
 
    },
    stateGet(){
      let _this = this;
      this.axios({
        method: "get",
        url: "/sign/isSignInToday",
        params: {
          userId: this.userId,
        }
      })
        .then(
          function(response) {
            if(response.data.isSign == true){
            this.stateFlag = true;
            }else{
              this.stateFlag = false;
            }
          }.bind(this)
        )
        .catch(
          function(error) {
            this.$Message.error("签到失败");
          }.bind(this)
        );
    },
    userSet(e) {
      this.user.loginName = e.loginName;
      this.user.email = e.email;
      this.user.headimg = e.headimg;
      this.user.name = e.name;
    },
    search() {
      if (
        this.$route.name == "home" ||
        this.$route.name == "page-home-title" ||
        this.$route.name == "page-home"
      ) {
        this.$router.push("/page/home/" + this.searchValue);
      } else if (this.$route.name == "article-home") {
        this.$router.push("/article" + "?searchValue=" + this.searchValue);
      }
    },
    menuSelect(e) {
      if (e == 1) {
        this.$router.push("/page/user");
      } else if (e == 2) {
        this.emailModal = true;
      } else if (e == 31) {
        this.$router.push("/article/create");
      } else if (e == 32) {
        this.$router.push("/article/user");
      } else if (e == 4) {
        this.$store.dispatch("users/loginOUt", { router: this.$router });
      } else if (e == 5) {
        this.$router.push("/base");
      } else if (e == 6) {
        this.$router.push("/login");
      } else if (e == "interest") {
        this.$router.push("/");
      } else if (e == "article") {
        this.$router.push("/article");
      } else if (e == 7) {
        this.stateModal = true;
      }
    },
    backHome() {
      this.$router.push("/page/home");
    },
    cancel() {
      this.$Message.info("点击了取消");
    },
    emailOk(email) {
      this.$refs[email].validate(valid => {
        if (valid) {
          this.axios({
            method: "post",
            url: "/email",
            data: this.email
          })
            .then(
              function(response) {
                this.$Message.info("发送成功");
              }.bind(this)
            )
            .catch(function(error) {
              alert(error);
            });
          this.emailModal = false;
        } else {
          this.$Message.error("表单验证失败!");
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        }
      });
    },
    publish() {
      //this.modal_loading = true;
      this.axios({
        method: "post",
        url: "/sign/addSignIn",
        params: {
          signState: 1,
          userId: this.userId,
        }
      })
        .then(
          function(response) {
            this.$Message.info("签到成功");
            if(response.data == "签到成功"){
              this.stateFlag = true;
              // console.log(this.stateModal);
            }
          }.bind(this)
        )
        .catch(
          function(error) {
            alter(error);
          }.bind(this)
        );
        this.stateModal = false;
    },
    /*登录*/
    login(code, state) {
      if (code != null && code != "" && state != null && state != "") {
        this.$Spin.show({
          render: h => {
            return h("div", [
              h("Icon", {
                style: {
                  animation: "ani-demo-spin 1s linear infinite"
                },
                props: {
                  type: "load-c",
                  size: 18
                }
              }),
              h("div", "正在登录，请等待...")
            ]);
          }
        });
        setTimeout(() => {
          this.$Spin.hide();
        }, 10000);
        if (state == "github") {
          this.githubLogin(code);
        } else if (state == "qq") {
          this.qqLogin(code);
        } else {
          this.$router.push({ path: "/" });
          location.reload();
        }
      } else {
        this.userGet();
      }
    },
    /*github登录*/
    githubLogin(code) {
      this.axios({
        method: "post",
        url: "/authentication/github",
        params: {
          code: code
        },
        auth: {
          username: "client",
          password: "secret"
        }
      })
        .then(
          function(response) {
            localStorage.setItem(
              "currentUser_token",
              response.data.access_token
            );
            localStorage.setItem(
              "currentUser_refresh_token",
              response.data.refresh_token
            );
            this.axios.defaults.headers.common["Authorization"] =
              "bearer " + localStorage.getItem("currentUser_token");
            this.$router.push({ path: "/" });
            location.reload();
          }.bind(this)
        )
        .catch(
          function(error) {
            this.$Message.error("登陆失败");
          }.bind(this)
        );
    },
    /*qq登录*/
    qqLogin(code) {
      this.axios({
        method: "post",
        url: "/authentication/qq",
        params: {
          code: code
        },
        auth: {
          username: "client",
          password: "secret"
        }
      })
        .then(
          function(response) {
            localStorage.setItem(
              "currentUser_token",
              response.data.access_token
            );
            localStorage.setItem(
              "currentUser_refresh_token",
              response.data.refresh_token
            );
            this.axios.defaults.headers.common["Authorization"] =
              "bearer " + localStorage.getItem("currentUser_token");
            this.$router.push({ path: "/qq" });
            this.$router.push({ path: "/" });
            location.reload();
          }.bind(this)
        )
        .catch(
          function(error) {
            this.$Message.error("登陆失败");
          }.bind(this)
        );
    },

    toMessages() {
      console.log("to messages page");
      this.$router.push({ path: "/page/messages" });
    }
  }
};
</script>

import Vue from "vue";
import VueRouter from "vue-router";
import chatArea from "../views/chatArea.vue";
import login from "../views/login.vue";
import addressBook from "../views/addressBook.vue";


Vue.use(VueRouter);

const routes = [
  {
    path: "/chatArea",
    name: "chatArea",
    component: chatArea
  },
  {
    path: "/addressBook",
    name: "addressBook",
    component: addressBook
  },
  {
    path: "/login",
    name: "login",
    component: login
  },
  {
    path: "/**",
    redirect: "/login"
  }
];

const router = new VueRouter({
  routes
});

export default router;

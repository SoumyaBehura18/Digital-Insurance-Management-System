import { createStore } from "vuex";
import claimsStore from "./ClaimsManagementStore/index.js";
// import userStore from './UserStore/index.js'
// import policiesStore from './UserPolicyStore/index.js'
import userStore from "@/store/UserStore/index.js";
import userPoliciesStore from "@/store/UserPolicyStore/index.js";
import policiesStore from "@/store/PolicyStore/index.js";
import ticketStore from "@/store/UserTicketStore/index.js";
import adminTicketStore from "@/store/TicketStore/index.js";
import adminPolicyStore from "@/store/AdminPolicyStore/index.js";

export default createStore({
  modules: {
    claims: claimsStore, // module name for claims
    user: userStore, // module name for user
    userPolicies: userPoliciesStore, // module name for user policies
    policies: policiesStore, // module name for all policies
    tickets: ticketStore, // module name for tickets
    adminTickets: adminTicketStore,
    adminPolicyStore: adminPolicyStore,
  },
});

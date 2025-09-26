import { createStore } from 'vuex'
//import claimsStore from './ClaimsManagementStore/index.js'
import userStore from './UserStore/index.js'
import policiesStore from './UserPolicyStore/index.js'

export default createStore({
  modules: {
    //claims: claimsStore, // module name for claims
    user: userStore ,     // module name for user
    policies: policiesStore // module name for user policies
  }
})

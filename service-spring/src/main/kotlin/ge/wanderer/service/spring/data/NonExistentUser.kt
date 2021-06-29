package ge.wanderer.service.spring.data

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService

fun nonExistentUser() = User("-1", "No", "User", 0, false)

fun getRequestingUser(id: String?, userService: UserService) = id?.run { userService.findUserById(id) } ?: run { nonExistentUser() }
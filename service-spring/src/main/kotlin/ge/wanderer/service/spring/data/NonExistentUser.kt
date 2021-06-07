package ge.wanderer.service.spring.data

import ge.wanderer.core.integration.user.User
import ge.wanderer.core.integration.user.UserService

fun nonExistentUser() = User(-1L, "No", "User", 0, false)

fun getRequestingUser(id: Long?, userService: UserService) = id?.run { userService.findUserById(id) } ?: run { nonExistentUser() }
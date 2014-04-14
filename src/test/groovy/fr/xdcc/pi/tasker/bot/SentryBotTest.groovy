package fr.xdcc.pi.tasker.bot

import fr.xdcc.pi.tasker.service.TaskerService
import org.jibble.pircbot.User
import spock.lang.Specification

class SentryBotTest extends Specification {

  SentryBot sentryBot
  TaskerService taskerService

  def setup() {
    sentryBot = new SentryBot("Sentry")
    taskerService = Mock(TaskerService)
    sentryBot.setTaskerService(taskerService)
  }

  def "parses the senders properly"() {
    given: "some mocked users"
    User[] userList = generateUsers()

    when: "OnUserList event is fired"
    sentryBot.onUserList("#serial_us", userList)

    then: "only the well named senders should be taken into account without the role character"
    1 * taskerService.updateAvailableBots([
        userList[0].nick.substring(1),
        userList[1].nick.substring(1),
        userList[2].nick.substring(1),
        userList[3].nick.substring(1)
    ])
  }

  def "user list of an unknow channel is received"() {
    when: "OnUserList event is fired with an unknown channel name"
    sentryBot.onUserList("#random", null)

    then: "an AssertionError should be thrown"
    thrown(AssertionError)
  }

  private User[] generateUsers() {
    User user1 = Mock(User)
    User user2 = Mock(User)
    User user3 = Mock(User)
    User user4 = Mock(User)
    User user5 = Mock(User)
    User user6 = Mock(User)
    User user7 = Mock(User)

    user1.getNick() >> "%[SeriaL]user1"
    user2.getNick() >> "%[DarksiDe]user2"
    user3.getNick() >> "%[Darkside]user3"
    user4.getNick() >> "%iNFEXiOUSuser4"
    user5.getNick() >> "[SeriaL]user5"
    user6.getNick() >> "user6"
    user7.getNick() >> "%user7"

    return [user1, user2, user3, user4, user5, user6, user7]
  }
}

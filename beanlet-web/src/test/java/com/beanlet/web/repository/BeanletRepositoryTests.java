package com.beanlet.web.repository;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BeanletRepositoryTests {

  @Autowired
  private BeanletRepository beanletRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindOne() {
    Beanlet beanlet = beanletRepository.findOne(1);
    assertThat(beanlet.getName()).isEqualTo("exercise");
    DateTime dateLastLogged = beanlet.getDateLastLogged().withZone(DateTimeZone.UTC);
    assertThat(dateLastLogged.getYear()).isEqualTo(2016);
    assertThat(dateLastLogged.getMonthOfYear()).isEqualTo(7);
    assertThat(dateLastLogged.getDayOfMonth()).isEqualTo(1);
    assertThat(dateLastLogged.getHourOfDay()).isEqualTo(12);
    assertThat(dateLastLogged.getMinuteOfHour()).isEqualTo(0);
    assertThat(dateLastLogged.getSecondOfMinute()).isEqualTo(0);
  }

  @Test
  public void testSaveBeanlet() {
    Beanlet beanlet = new Beanlet();
    beanlet.setName("scripture reading");
    beanlet.setUser(userRepository.findOne(1));
    beanlet.setDateLastLogged(new DateTime());
    beanletRepository.save(beanlet);

    assertThat(beanlet.getId()).isNotNull();
    beanlet = beanletRepository.findOne(beanlet.getId());
    assertThat(beanlet.getName()).isEqualTo("scripture reading");
    assertThat(beanlet.getDateCreated()).isNotNull();
    assertThat(beanlet.getDateUpdated()).isNotNull();
    assertThat(beanlet.getDateLastLogged()).isNotNull();
    assertThat(beanlet.getVersion()).isNotNull();
  }

  @Test
  public void testFindAllByUserId() {
    createBeanlet("mar", new DateTime(2016, 3, 1, 1, 1, 1, 1));
    createBeanlet("jan", new DateTime(2016, 1, 1, 1, 1, 1, 1));
    createBeanlet("feb", new DateTime(2016, 2, 1, 1, 1, 1, 1));
    List<Beanlet> beanlets = beanletRepository.findAllByUserId(2);
    assertThat(beanlets).isNotNull().hasSize(3);
    assertThat(beanlets.get(0).getName()).isEqualTo("jan");
    assertThat(beanlets.get(1).getName()).isEqualTo("feb");
    assertThat(beanlets.get(2).getName()).isEqualTo("mar");
  }

  @Test
  public void testCreateBeanletInUtc() {
    User user = new User();
    user.setId(1);
    Beanlet beanlet = new Beanlet(user, "UTC you know it");
    beanlet.setDateLastLogged(new DateTime(2000, 6, 1, 12, 0, 0, DateTimeZone.forID("America/Chicago")));
    beanletRepository.save(beanlet);
  }

  private Beanlet createBeanlet(String name, DateTime lastLogged) {
    Beanlet beanlet = new Beanlet();
    beanlet.setName(name);
    beanlet.setUser(userRepository.findOne(2));
    beanlet.setDateLastLogged(lastLogged);
    beanletRepository.save(beanlet);
    return beanlet;
  }

}
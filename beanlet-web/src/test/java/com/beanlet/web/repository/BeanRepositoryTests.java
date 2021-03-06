package com.beanlet.web.repository;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.beanlet.web.TestUtils.*;
import static com.beanlet.web.TestUtils.uuid;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BeanRepositoryTests {

  @Autowired
  private BeanRepository beanRepository;

  @Test
  public void testPersisteBean() {
    Bean bean = buildBean();
    beanRepository.save(bean);
    assertThat(bean.getId()).isNotNull();
    assertThat(bean.getId().length()).isEqualTo(32);
  }

  @Test
  public void testFindOneBean() {
    Bean bean = beanRepository.findOne(BEAN_ID);
    assertThat(bean).isNotNull();
    assertThat(bean.getId()).isEqualTo(BEAN_ID);
    assertThat(bean.getBeanletId()).isEqualTo(EXERCISE);
    assertThat(bean.getUtcDate()).isEqualTo(new DateTime(2016, 7, 1, 12, 0, 0));
    assertThat(bean.getLocalDate()).isEqualTo(new DateTime(2016, 7, 1, 7, 0, 0));
  }

  @Test
  public void testFindByBeanletId_indexIsOrderingBeansCorrectly() {
    List<Bean> beans = beanRepository.findByBeanletId(EXERCISE, new Sort(Sort.Direction.ASC, "localDate"));
    assertThat(beans).isNotNull().isNotEmpty();
    assertThat(beans.size()).isEqualTo(3);
    assertThat(beans.get(0).getLocalDate().getMonthOfYear()).isEqualTo(6);
    assertThat(beans.get(1).getLocalDate().getMonthOfYear()).isEqualTo(6);
    assertThat(beans.get(2).getLocalDate().getMonthOfYear()).isEqualTo(7);
  }

  @Test
  public void testCountByBeanletIdAndLocalDateBetween() {
    DateTime start = new DateTime(2016, 6, 1, 0, 0, 0);
    DateTime end = new DateTime(2016, 6, 30, 23, 59, 59);
    Long result = beanRepository.countByBeanletIdAndLocalDateBetween(EXERCISE, start, end);
    assertThat(result).isEqualTo(2);
  }

  @Test
  public void testFindFirstByBeanletIdOrderByLocalDateDesc() {
    Bean bean = beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(EXERCISE);
    assertThat(bean).isNotNull();
    assertThat(bean.getId()).isEqualTo(new EntityId<>("ae3018d456114794a5d35ba7d5a4d180"));
  }

  @Test
  public void testFindFirstByBeanletIdOrderByLocalDateDesc_noResult() {
    Bean bean = beanRepository.findFirstByBeanletIdOrderByLocalDateDesc(
      new EntityId<>("ae1238d456114794a5d35ba7d5a4d180"));
    assertThat(bean).isNull();
  }

  @Test
  public void testCountByBeanletId() {
    assertThat(beanRepository.countByBeanletId(EXERCISE)).isEqualTo(3);
    assertThat(beanRepository.countByBeanletId(SCRIPTURE_READING)).isEqualTo(0);
  }

  @Test
  public void testDeleteAllByBeanletId() {
    Bean bean = buildBean();
    beanRepository.save(bean);
    assertThat(beanRepository.countByBeanletId(bean.getBeanletId())).isEqualTo(1);
    beanRepository.deleteAllByBeanletId(bean.getBeanletId());
    assertThat(beanRepository.countByBeanletId(bean.getBeanletId())).isEqualTo(0);
  }

  private Bean buildBean() {
    EntityId<Beanlet> beanletId = new EntityId<>(uuid());
    Bean bean = new Bean();
    bean.setBeanletId(beanletId);
    DateTime date = new DateTime(2016, 8, 1, 12, 0, 0, DateTimeZone.forID("America/Chicago"));
    bean.setUtcDate(date);
    bean.setLocalDate(date.withZoneRetainFields(DateTimeZone.UTC));
    bean.setLocalTimeZone(DateTimeZone.forID("America/Chicago"));
    return bean;
  }

}

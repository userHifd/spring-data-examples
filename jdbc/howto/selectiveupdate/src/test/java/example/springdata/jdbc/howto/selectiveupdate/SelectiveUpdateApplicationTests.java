package example.springdata.jdbc.howto.selectiveupdate;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SelectiveUpdateApplicationTests {

	@Autowired MinionRepository minions;
	@Autowired PlainMinionRepository plainMinions;


	@Test
	void renameWithReducedView() {

		Minion bob = new Minion("Bob")
				.addToy(new Toy("Tiger Duck"))
				.addToy(new Toy("Security blanket"));
		minions.save(bob);

		PlainMinion plainBob = plainMinions.findById(bob.id).orElseThrow();
		plainBob.name = "Bob II.";
		plainMinions.save(plainBob);

		Minion bob2 = minions.findById(bob.id).orElseThrow();

		assertThat(bob2.toys).containsExactly(bob.toys.toArray(new Toy[]{}));
		assertThat(bob2.name).isEqualTo("Bob II.");
		assertThat(bob2.color).isEqualTo(Color.YELLOW);
	}

	@Test
	void turnPurpleByDirectUpdate() {

		Minion bob = new Minion("Bob")
				.addToy(new Toy("Tiger Duck"))
				.addToy(new Toy("Security blanket"));
		minions.save(bob);

		minions.turnPurple(bob.id);

		Minion bob2 = minions.findById(bob.id).orElseThrow();

		assertThat(bob2.toys).containsExactly(bob.toys.toArray(new Toy[]{}));
		assertThat(bob2.name).isEqualTo("Bob");
		assertThat(bob2.color).isEqualTo(Color.PURPLE);
	}

	@Test
	void grantPartyHat() {

		Minion bob = new Minion("Bob")
				.addToy(new Toy("Tiger Duck"))
				.addToy(new Toy("Security blanket"));
		minions.save(bob);

		minions.addPartyHat(bob);

		Minion bob2 = minions.findById(bob.id).orElseThrow();

		assertThat(bob2.toys).extracting("name").containsExactlyInAnyOrder("Tiger Duck", "Security blanket", "Party Hat");
		assertThat(bob2.name).isEqualTo("Bob");
		assertThat(bob2.color).isEqualTo(Color.YELLOW);
		assertThat(bob2.version).isEqualTo(bob.version+1);

		assertThatExceptionOfType(OptimisticLockingFailureException.class).isThrownBy(() -> minions.addPartyHat(bob));
	}

}

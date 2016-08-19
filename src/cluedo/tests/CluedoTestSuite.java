package cluedo.tests;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The test suite for the Cluedo program
 * @author aidandoak
 *
 */

@RunWith(Suite.class)

@Suite.SuiteClasses({
	CharacterTests.class,
	BoardTests.class,
	RoomTests.class,
	PlayerTests.class,
	MoveTests.class
})
public class CluedoTestSuite {

}

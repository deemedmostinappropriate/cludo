package cluedo.test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import cluedo.test.suite.BoardTests;
import cluedo.test.suite.CharacterTests;
import cluedo.test.suite.MoveTests;
import cluedo.test.suite.PlayerTests;
import cluedo.test.suite.RoomTests;

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

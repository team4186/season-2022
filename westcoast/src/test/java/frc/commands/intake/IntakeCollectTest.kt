package frc.commands.intake

import edu.wpi.first.wpilibj.util.Color
import frc.subsystems.IntakeSubsystem
import frc.subsystems.MagazineSubsystem
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class IntakeCollectTest : StringSpec({

    "when intake collect starts it should collect until it is full" {
        val intake = mockk<IntakeSubsystem>(relaxed = true)
        val magazine = mockk<MagazineSubsystem>(relaxed = true)
        val wantedColor = Color(1.0, 0.0, 0.0)

        var feederFull = false
        var indexFull = false
        var colorMatched = false

        every { magazine.hasFeederSensorBreak() } answers { feederFull }
        every { magazine.hasIndexSensorBreak() } answers { indexFull }
        every { magazine.isMatchingColor(wantedColor) } answers { colorMatched }

        excludeRecords { @Suppress("UnusedEquals") intake.equals(magazine) }
        excludeRecords { @Suppress("UnusedEquals") magazine.equals(intake) }

        val command = IntakeCollect(
            intake,
            magazine,
            wantedColor,
            1,
            1,
            true
        )

        command.initialize()

        // start collection
        command.execute()

        // accept ball
        indexFull = true
        colorMatched = true
        command.execute()

        // send to feeder
        command.execute()

        // arrive at feeder
        feederFull = true
        indexFull = false
        colorMatched = false
        command.execute()

        // restart collection
        command.execute()

        // not accept ball
        indexFull = true
        command.execute()

        // reject ball
        command.execute()
        indexFull = false
        command.execute()

        // accept ball
        command.execute()
        indexFull = true
        colorMatched = true
        command.execute()
        // reverse intake
        command.execute()
        // stop intake
        command.execute()

        // stop collecting
        command.execute()

        verifySequence {
            // execute start collection
            magazine.hasIndexSensorBreak()
            intake.start()
            magazine.startIndexMotor()

            // execute accept ball
            magazine.hasIndexSensorBreak()
            intake.stop()
            magazine.stopIndexMotor()
            magazine.isMatchingColor(wantedColor)
            magazine.hasFeederSensorBreak()

            // execute send to feeder
            magazine.hasFeederSensorBreak()
            magazine.startIndexMotor()
            magazine.startFeederMotor()

            // execute arrive at feeder
            magazine.hasFeederSensorBreak()
            magazine.stopIndexMotor()
            magazine.stopFeederMotor()

            // execute restart collection
            magazine.hasIndexSensorBreak()
            intake.start()
            magazine.startIndexMotor()

            // execute not accept ball
            magazine.hasIndexSensorBreak()
            intake.stop()
            magazine.stopIndexMotor()
            magazine.isMatchingColor(wantedColor)

            // execute reject ball
            magazine.startIndexMotor()
            magazine.startRejectMotor()
            magazine.stopIndexMotor()
            magazine.stopRejectMotor()

            // execute accept ball
            magazine.hasIndexSensorBreak()
            intake.start()
            magazine.startIndexMotor()
            magazine.hasIndexSensorBreak()
            intake.stop()
            magazine.stopIndexMotor()
            magazine.isMatchingColor(wantedColor)
            magazine.hasFeederSensorBreak()

            // reject
            intake.reverse()
            intake.stop()

            // stop collecting
            magazine.stopAll()
            intake.stop()
        }
        confirmVerified(intake, magazine)

        command.isFinished shouldBe true
    }
})
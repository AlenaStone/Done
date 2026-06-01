package com.done.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        migrateTaskTable(db)
        migrateAssignmentTable(db)
        migrateExamTable(db)
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `Course` ADD COLUMN `totalClasses` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `Course` ADD COLUMN `missedClasses` INTEGER NOT NULL DEFAULT 0")
    }
}

private fun migrateTaskTable(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE `Task` RENAME TO `Task_old`")
    db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `Task` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `courseId` INTEGER NOT NULL,
            `name` TEXT NOT NULL,
            `isDone` INTEGER NOT NULL,
            `deadline` TEXT NOT NULL,
            FOREIGN KEY(`courseId`) REFERENCES `Course`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
        )
        """.trimIndent()
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS `index_Task_courseId` ON `Task` (`courseId`)")
    db.execSQL(
        """
        INSERT INTO `Task` (`id`, `courseId`, `name`, `isDone`, `deadline`)
        SELECT `id`, `courseId`, `name`, `isDone`, `deadline`
        FROM `Task_old`
        WHERE EXISTS (
            SELECT 1 FROM `Course`
            WHERE `Course`.`id` = `Task_old`.`courseId`
        )
        """.trimIndent()
    )
    db.execSQL("DROP TABLE `Task_old`")
}

private fun migrateAssignmentTable(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE `Assignment` RENAME TO `Assignment_old`")
    db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `Assignment` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `courseId` INTEGER NOT NULL,
            `title` TEXT NOT NULL,
            `date` TEXT NOT NULL,
            `isDone` INTEGER NOT NULL,
            `note` REAL,
            FOREIGN KEY(`courseId`) REFERENCES `Course`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
        )
        """.trimIndent()
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS `index_Assignment_courseId` ON `Assignment` (`courseId`)")
    db.execSQL(
        """
        INSERT INTO `Assignment` (`id`, `courseId`, `title`, `date`, `isDone`, `note`)
        SELECT `id`, `courseId`, `title`, `date`, `isDone`, `note`
        FROM `Assignment_old`
        WHERE EXISTS (
            SELECT 1 FROM `Course`
            WHERE `Course`.`id` = `Assignment_old`.`courseId`
        )
        """.trimIndent()
    )
    db.execSQL("DROP TABLE `Assignment_old`")
}

private fun migrateExamTable(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE `Exam` RENAME TO `Exam_old`")
    db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `Exam` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `courseId` INTEGER NOT NULL,
            `title` TEXT NOT NULL,
            `date` TEXT NOT NULL,
            `isDone` INTEGER NOT NULL,
            `note` REAL,
            FOREIGN KEY(`courseId`) REFERENCES `Course`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
        )
        """.trimIndent()
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS `index_Exam_courseId` ON `Exam` (`courseId`)")
    db.execSQL(
        """
        INSERT INTO `Exam` (`id`, `courseId`, `title`, `date`, `isDone`, `note`)
        SELECT `id`, `courseId`, `title`, `date`, `isDone`, `note`
        FROM `Exam_old`
        WHERE EXISTS (
            SELECT 1 FROM `Course`
            WHERE `Course`.`id` = `Exam_old`.`courseId`
        )
        """.trimIndent()
    )
    db.execSQL("DROP TABLE `Exam_old`")
}

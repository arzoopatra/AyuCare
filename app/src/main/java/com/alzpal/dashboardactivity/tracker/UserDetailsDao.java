package com.alzpal.dashboardactivity.tracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDetailsDao {

    /**
     * Inserts a UserDetails object into the database.
     *
     * @param userDetails The UserDetails object to be inserted.
     */
    @Insert
    void insert(UserDetails userDetails);

    /**
     * Retrieves all UserDetails objects from the database.
     *
     * @return A list of UserDetails objects.
     */
    @Query("SELECT * FROM UserDetails")
    List<UserDetails> getAll();

    /**
     * Deletes a UserDetails object from the database.
     *
     * @param userDetails The UserDetails object to be deleted.
     */
    @Delete
    void delete(UserDetails userDetails); // Add this method to delete a user
}


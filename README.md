# Voltage - note taking app
Note taking app with cool functions powred by Java.
- Model-View-Controller pattern.
- All data is saved to a local database.


## Notes
A simple user interface has been created for notes with display and creation of notes

### Main

Main notes screen           |  Add/Edit notes screen
:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/51397432/180619800-0b45a278-bc52-4464-b31d-e7f624720077.png)  |  ![](https://user-images.githubusercontent.com/51397432/180619967-0421d465-b232-4dac-b771-34fd0b1ed75e.png)

### Recycle bin and 
Deleted notes can stay in the trash indefinitely. The search for notes in the database is carried out character by character.

Recycle bin screen         |  Search screen
:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/51397432/180620204-461ee9e2-dc5e-4457-82dc-23c2f6e6dbdf.png) | ![](https://user-images.githubusercontent.com/51397432/180620211-96f18d86-b3b0-4e07-8eca-e558b4f16c12.png)

### Note ViewModel
Controller is used to save and display note data from local database

``` java
...

    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

...

    public void insert(Note note) {
        repository.insert(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
...

```

## Tasks
In order to create a task, you need to create a folder. Folder names can be changed. And if 
you want to delete the entire folder, the program will warn you that all created tasks in this folder will be deleted.
### UI

- Main folder screen
The main screen displays all created folders. By long pressing on the list item, the bottom
menu will open, and in which it is possible to rename or delete the folder

- Main tasks screen
On the main task screen, all created tasks are displayed in the form of a convenient search.

- Task data screen
On the task data screen, you can add a due date to the task and add additional data about the task.

Main folder screen         |  Main tasks screen        | Task data screen            
:-------------------------:|:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/51397432/180620333-b0fb2a93-4a14-4c36-8f23-eb794acbbe6b.png) | ![](https://user-images.githubusercontent.com/51397432/180620491-669a7858-bdfe-4460-8a3a-5cb8ae62dc3e.png) | ![](https://user-images.githubusercontent.com/51397432/180620496-ca1f281c-7fcd-43a2-90d7-8e378887dfb2.png)

## Passwords and passwords generator

### UI

Main passwords screen      |  Password screen          | Password generator screen            
:-------------------------:|:-------------------------:|:-------------------------:
![](https://user-images.githubusercontent.com/51397432/180620604-b88310b8-a311-40d0-afa1-199c537a951a.png) | ![](https://user-images.githubusercontent.com/51397432/180620606-d7e2fcce-540f-42d5-9a56-88f70d81522b.png) | ![](https://user-images.githubusercontent.com/51397432/180620609-f405814e-5520-4071-98e1-04e22c43c78f.png)


## Searh data in database
For searching data in database used basic SQL Query.

``` java
@Query("SELECT * FROM note_table WHERE title LIKE '%' || :search  || '%' OR description LIKE '%' || :search  || '%'")
LiveData<List<Note>> findNote(String search);
```


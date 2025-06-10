# Architecture Overview

This document describes the architectural design of the project, 
including high-level structure, core components, data flow, and development principles.

## Version History

| Version | Date       | Changes
|---------|------------|---------
| 1.0     | 2025-05-05 | Initial version created

---

## 1. Goals and Principles

- Use **MVVM (Model-View-ViewModel)** as the primary architecture pattern
- Ensure **separation of concerns**, testability, and modularity
- Follow **single responsibility principle (SRP)** in each layer
- Support **scalable navigation and state management**

## 2. Environment Requirements

### 2.1 Hardware Requirements
- **CPU**: Minimum 1.2 GHz dual-core processor
- **RAM**: At least 1 GB
- **Storage**: Minimum 50 MB for app installation + extra space for images (recommended: 500 MB)
- **Camera**: Rear camera with at least 5 MP resolution (recommended for acceptable image quality)
- **Display**: Minimum 4.5" screen, resolution 720x1280 px

### 2.2 Software Requirements
- **Android Version**: Minimum Android 6.0 (API level 23)
- **Database Support**: Built-in SQLite (provided by Android system)

---

## 3.1 High-Level Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/maalelan/postcardstorehouse/
│   │   │   ├── activities/                         # Activities
│   │   │   ├── adapters/                           # 
│   │   │   ├── fragments/                          # UI fragments (View layer)
|   |   |   ├── viewmodels/                         # ViewModel layer
│   │   │   ├── repositories/                       # Repository layer
│   │   │   ├── models/                             # Data models (Model layer)
│   │   │   │   └── database/                       # Database operations and entities
│   │   │   │       ├── entities/ 
│   │   │   │       └── dao/
│   │   │   ├── navigation/                         # Navigation
│   │   │   └── utils/                              # Helper classes
│   │   └── res/                                    # Resources
│   │       ├── layout/                             # Fragment/activity layouts
│   │       ├── menu/                               # Menu resources (e.g., bottom nav items)
│   │       ├── navigation/                         # Navigation graph
│   │       └── values/                             # Strings, colors, themes, etc.
└── build.gradle                                    # Gradle-config.
```

---
## 3.2 Detailed source layout

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/maalelan/postcardstorehouse/
│   │   │   ├── activities/                         # Activities
│   │   │   │   └── MainActivity.java               # Host activity for fragments & bottom nav
│   │   │   ├── adapters/                           # 
│   │   │   │   └── PostcardAdapter.java            # 
│   │   │   ├── fragments/                          # UI fragments (View layer)
│   │   │   │   ├── HomeFragment.java               # Home / Landing page
│   │   │   │   ├── GalleryFragment.java            # Postcard gallery list
│   │   │   │   ├── AddPostcardFragment.java        # Form to add (/edit old..) a new postcard
│   │   │   │   └── DetailsFragment.java            # View/edit details of selected postcard
|   |   |   ├── viewmodels/                         # ViewModel layer
│   │   │   │   ├── PostcardViewModel.java          # Manages UI-related postcard data
│   │   │   │   ├── HomeViewModel.java              # Manages HomeFragment presentation logic
│   │   │   │   └── ImageViewModel.java             # Manages image-related operations
│   │   │   ├── repositories/                       # Repository layer
│   │   │   │   ├── PostcardRepository.java         # Data source abstraction
│   │   │   │   └── ImageRepository.java            # Image storage abstraction
│   │   │   ├── models/                             # Data models (Model layer)
│   │   │   │   ├── Postcard.java                   # Data model for business logic
│   │   │   │   ├── PostcardImage.java              # Data model for business logic
│   │   │   │   ├── FilterCriteria.java             # Filter model
│   │   │   │   └── database/                       # Database operations and entities
│   │   │   │       ├── entities/ 
│   │   │   │       │   └── PostcardEntity.java     # Room Entity
│   │   │   │       ├── dao/
│   │   │   │       │   └── PostcardDao.java        # DAO (Data Access Object)
│   │   │   │       └── PostcardDatabase.java       # Room Database
│   │   │   ├── navigation/                         # Navigation
│   │   │   │   ├── NavigationManager.java          # 
│   │   │   │   └── NavigationEvent.java            # 
│   │   │   └── utils/                              # Helper classes
|   │   │       ├── DateUtils.java                  # Date formatting, etc.
|   │   │       ├── Event.java                      # Navigation
│   │   │       ├── ImageUtils.java                 # Image processing functions
│   │   │       ├── InputValidator.java             # checks input formats, return true/false/error msg
│   │   │       └──PostcardMapper.java             # POJO <-> Entity
│   │   └── res/                                    # Resources
│   │       ├── layout/                             # Fragment/activity layouts
│   │       ├── menu/                               # Menu resources (e.g., bottom nav items)
│   │       │   └── bottom_nav_menu.xml             # Bottom nav config
│   │       ├── navigation/                         # Navigation graph
│   │       │   └── nav_graph.xml                   # Defines fragment destinations
│   │       └── values/                             # Strings, colors, themes, etc.
└── build.gradle                                    # Gradle-config.
```

---

### 4.1 Component Structure (MVVM)

+------------------+ +---------------------+ +--------------------+
|            |                               |                    |
| View       | <--->   | ViewModel |   <---> | Model              |
| (UI Layer) |   | (Presentation Logic)|     | (Data Layer)       |
+------------------+ +---------------------+ +--------------------+
       |                       |                   |
       v                       v                   v
+------------------+ +---------------------+ +--------------------+
| Activities    | | UI State (LiveData) | | Room Database         |
| Fragments     | | UI Events           | | Entities & DAOs       |
| Layouts (XML) | | Handles user input  | | Repositories          |
| Data Binding  | | Calls Repository    | | (local/remote)        |
+------------------+ +---------------------+ +--------------------+

## 5. Architectural Layers

### 5.1 View (UI Layer)
- **Fragments/Activities** render UI and observe ViewModels
- Uses **Data Binding** and **LiveData** for reactivity

### 5.2 ViewModel
- Holds UI-related data and state
- Communicates with Repository
- Survives configuration changes

### 5.3 Repository
- Abstracts access to data sources
- Coordinates between local DB and (future) remote APIs

### 5.4 Data Layer
- **Room** for local persistence
  - Entities, DAOs, Database instance
- (Future) network layer: Retrofit

---

## 6. Data Model

### 6.1 Database Tables

#### Postcard

```
+----------------+-------------+------------------------------ +
| Field          | Type        | Description                   |
+----------------+-------------+------------------------------ +
| id             | INTEGER     | Primary key (autoincr)        |
| isSentByUser   | BOOLEAN     | True = sent, False = received |
| senderCountry  | TEXT        | Country of sender             |
| receivedDate   | TIMESTAMP   | Date the postcard was received|
| sentDate       | TIMESTAMP   | Date the postcard was sent    |
| topic          | TEXT        | Postcard topic                |
| notes          | TEXT        | user notes                    |
| isFavorite     | BOOLEAN     | marked as favorite            |
| createdAt      | TIMESTAMP   | Record creation timestamp     | 
+----------------+-------------+-------------------------------+
```
- `isSentByUser`: `false` means the postcard was received (default),
  `true` means the postcard was sent by the user.

#### PostcardImage
```
+----------------+-------------+------------------------+
| Field          | Type        | Description            |
+----------------+-------------+------------------------+
| id             | INTEGER     | Primary key (autoincr) |
| postcardId     | INTEGER     | Foreign key to postcard|
| tagName        | TEXT        | Image tag              |
| imageUri       | TEXT        | Local URI of the image |
+----------------+-------------+------------------------+
```
- `tagName` examples: `front`, `back`, `stamp`, `postmark`

### 6.2 Relationships

- One-to-many relationship between **Postcard** and **PostcardImage**  
  (A postcard can have multiple related images)

### 6.3 Data Access Objects (DAOs)

- `PostcardDao`: CRUD operations for postcards
- `PostcardImageDao`: CRUD operations for images (if implemented)

## 7. Key Functional Flows

### 7.1 Adding a New Postcard

Flow Overview:

  1.HomeFragment
    → User taps button to add a new postcard

  2.AddPostcardFragment
    → Allows capturing or selecting an image

  3.ImageUtils
    → Saves the image to device gallery (MediaStore)

  4.URI Retrieval
    → Gets URI of saved image
  
  5.PostcardViewModel
    → Validates form data, prepares Postcard object

  6.PostcardRepository
    → Inserts data into Room database

  7.LiveData Observer
    → HomeFragment observes data changes and updates RecyclerView

**Steps Summary**:
1. User navigates to the "Add Postcard" screen  
2. User captures a photo or picks one from gallery  
3. Image is stored in the device's media library  
4. The app receives a URI to the stored image  
5. User fills out postcard info → ViewModel validates and prepares data  
6. ViewModel uses Repository to insert data into Room  
7. UI is updated automatically through LiveData observer

---

### 7.2 Browsing and Filtering Postcards

Flow Overview:

  1.HomeFragment
    → Displays list of postcards

  2.User filters data
    → Filter options sent to ViewModel

  3.PostcardViewModel
    → Applies filter logic and triggers DB query

  4.PostcardRepository
    → Fetches filtered postcards from Room

  5.ImageUtils
    → Loads image URIs from local storage

  6.LiveData
    → Updates UI list automatically

### 7.3 Use of Adapters in the Postcard App

            Component | Adapter Used
----------------------|----------------------------
        Postcard list | PostcardAdapter (RecyclerView)
Postcard image viewer | ImagePagerAdapter (ViewPager)
 Tag list on postcard | TagAdapter (RecyclerView, horizontal layout)

**Adapter Types:**

1. RecyclerView.Adapter

   - Used for displaying scrollable lists or grids of items.
   - Example: vertical list of postcards in the main view.

2. ViewPagerAdapter / FragmentPagerAdapter

   - Used for displaying swipable screens or tabs.
   - Example: image carousel for a single postcard, showing front, back, stamps, etc.

    _Note: Currently only PostcardAdapter is implemented._
    _Other adapters (ImagePagerAdapter, TagAdapter) are planned but not yet in use._

**Steps Summary**:
1. User opens the home screen with a list of postcards  
2. User applies filters (e.g., favorites, sent vs received)  
3. ViewModel queries filtered data via Repository  
4. Repository loads related image URIs using local storage  
5. ViewModel exposes LiveData → UI observes and updates list view

## 8. Data Flow

1. UI event triggers a ViewModel method  
2. ViewModel calls Repository  
3. Repository fetches from local DB 
4. ViewModel exposes state via LiveData
5. UI observes and updates accordingly  

---

## 9. Technology Stack

- **Java**
- **Android Jetpack**
  - ViewModel, LiveData, Room, Navigation
- **Architecture Components**: MVVM
- **Testing** (planned): JUnit, Espresso

---

## 10. Future Improvements

- Add remote data source (e.g. REST API via Retrofit)


---

## 11. Related Documents

- [`README.md`](./README.md) – project summary
- [`CHANGELOG.md`](./changelog.md) – notable changes
- [`troubleshooting.md`](./troubleshooting.md) – known issues and workarounds


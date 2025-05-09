# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)

Types of changes for quick reminder.

    Added for new features.
    Changed for changes in existing functionality.
    Deprecated for soon-to-be removed features.
    Removed for now removed features.
    Fixed for any bug fixes.
    Security in case of vulnerabilities.


## [Unreleased]

### Changed
- Refactored application architecture from MVC to MVVM pattern
  - Navigation and HomeFragment updated
- Introduced `ViewModel` and `Repository` layers for better separation of concerns
- Room database:
- Field renamed from `theme` to `topic` in `PostcardEntity`
  - Database version increased to `2`

### Added
- Initial project structure defined
- `README.md` created with project description and folder overview
- `troubleshooting.md` created with first issues
- `pcsh-architecture.md` created with application architecture
- Data models: `Postcard.java`, `PostcardImage.java` created
- Room database setup: `PostcardEntity`, `PostcardImageEntity`, `PostcardDao`, and `PostcardDatabase`
- Room database .fallbackToDestructiveMigration() is deprecated 
  - -> migrations dir: `migration1to2`, for later use
- Bottom navigation + UI fragments
- Fragments setup: `AddPostcardFragment`,`GalleryFragment`, `HomeFragment`,`PostcardDetailsFragment`
- Res/navigation nav_graph.xml
- Res/menu bottom_nav_menu.xml
- Res/layout: home_fragment.xml, add_postcard_fragment.xml
- Drawable/ some icons, background image
- Dependencies
  - Navigation


### Removed
- Legacy MVC Controllers (e.g., `NavigationController`)

## [0.1.0] - 2025-04-13
### Added
- Project kickoff and base folder structure
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
- Refactored application architecture from MVC to MVVM pattern (in progress)
  - Navigation and HomeFragment changed
- Introduced ViewModel and Repository layers for better separation of concerns (in progress)
- Modified project structure to reflect partial migration to MVVM (in progress)

### Added
- Initial project structure defined
- `README.md` created with project description and folder overview
- `troubleshooting.md` created with first issues
- Postcard data model (`Postcard.java`) created
- PostcardImage data model (`PostcardImage.java`) created
- Room database setup: `PostcardEntity`, `PostcardDao`, and `PostcardDatabase`
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
- Controllers:
  - `NavigationController`

## [0.1.0] - 2025-04-13
### Added
- Project kickoff and base folder structure
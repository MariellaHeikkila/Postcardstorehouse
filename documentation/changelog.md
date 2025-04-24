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
### Added
- Initial project structure defined
- `README.md` created with project description and folder overview
- Postcard data model (`Postcard.java`) created
- PostcardImage data model (`PostcardImage.java`) created
- Room database setup: `PostcardEntity`, `PostcardDao`, and `PostcardDatabase`
- Room database .fallbackToDestructiveMigration() is deprecated 
  - -> migrations dir: `migration1to2`, for later use

## [0.1.0] - 2025-04-13
### Added
- Project kickoff and base folder structure
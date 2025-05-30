# Main Branches

## Database

| Class Name                            | Table                                                             | Notes                                 |
| :------------------------------------ | :---------------------------------------------------------------- | :------------------------------------ |
| [MainBranch](MainBranch.java)         | main_branches                                                     |                                       |
|                                       | [main_branches_view_materialized](MainBranchViewMaterialized.sql) | Materialized to join Associations.    |
| [MainBranchView](MainBranchView.java) | [main_branches_view](MainBranchView.sql)                          | Not materialized b/c joining 2 users. |

## Permissions

- All users can view main branches
- Only Admin role can edit main branches

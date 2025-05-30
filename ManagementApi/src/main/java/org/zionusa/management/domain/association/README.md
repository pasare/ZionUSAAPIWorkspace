# Associations

## Database

| Class Name                              | Table                                    | Notes                                 |
| :-------------------------------------- | :--------------------------------------- | :------------------------------------ |
| [Association](Association.java)         | associations                             |                                       |
| [AssociationView](AssociationView.java) | [associations_view](AssociationView.sql) | Not materialized b/c joining 2 users. |

## Permissions

- All users can view associations
- Only Admin role can edit associations

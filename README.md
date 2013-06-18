hql-generator
============

Generate hql from a parameter bean

In my early projects, we wrote many hql string in codes, these hqls make many problems. First, it's hard to test these hqls; Secondary, you maybe write many if/else to make the combine the conditions. So this project finally came out. It can generate hql from a bean with given annotations, so the IDE can check if your condition is right or not. 
And I even offer a call validator for you to test your QueryParamsCall.
You may use this tool in following condition:
1. You have many hql that written in java code, and most of them is "from ... where ..."
2. You have to query data with combining AND conditions. 

It not suitable for you when
1. You have many table joins in your hql.
2. You want to make dynamic condition from page for user to choose.
3. You have OR query.
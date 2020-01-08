
-- 分类获取二级分类的自连接
SELECT
	f.id as id,
	f.`name` as name,
	f.type as type,
	f.father_id as fatherId,
	c.id as subId,
	c.name as subName,
	c.type as subType,
	c.father_id as  subFatherId
FROM `category` f
left JOIN category c
on f.id = c.father_id
where f.father_id = 1

package base

import reactivemongo.bson.BSONObjectID

trait Model{
  def id: Option[BSONObjectID]
}


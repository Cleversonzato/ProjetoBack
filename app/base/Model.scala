package base

import reactivemongo.bson.BSONObjectID

abstract class Model(){
  def _id: BSONObjectID
}

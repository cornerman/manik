package MyFilter

import javax.inject.Inject

import play.filters.cors.CORSFilter
import play.api.http.{ DefaultHttpFilters, EnabledFilters }

class Filters @Inject()(enabledFilters: EnabledFilters, corsFilter: CORSFilter)
    extends DefaultHttpFilters(enabledFilters.filters :+ corsFilter: _*)



/*
import javax.inject.Inject
import play.api.http.DefaultHttpFilters
import play.api.http.EnabledFilters
import play.filters.gzip.GzipFilter

class Filters @Inject() (
                          defaultFilters: EnabledFilters,
                          gzip: GzipFilter,
                          log: LoggingFilter
                        ) extends DefaultHttpFilters(defaultFilters.filters :+ gzip :+ log: _*)
*/
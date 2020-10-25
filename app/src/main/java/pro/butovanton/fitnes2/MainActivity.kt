package pro.butovanton.fitnes2

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    lateinit var drawer : DrawerLayout
    lateinit var navController : NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_bind, R.id.nav_setting_server, R.id.nav_log), drawer)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                if (destination.id == R.id.nav_home) {
                    backIB.visibility = View.INVISIBLE
                    backConstrait.visibility = View.INVISIBLE
                    menuIB.visibility = View.VISIBLE
                    menuConstrait.visibility = View.VISIBLE
                }
                else {
                    backIB.visibility = View.VISIBLE
                    backConstrait.visibility = View.VISIBLE
                    menuIB.visibility = View.INVISIBLE
                    menuConstrait.visibility = View.INVISIBLE
                }
            textTitleFragment.text = destination.label
            }

        })
        backIB.setOnClickListener {
            navController.popBackStack()
        }
        menuIB.setOnClickListener {
            drawer.open()
        }
        menuConstrait.setOnClickListener{
             menuIB.performClick()
        }
        backConstrait.setOnClickListener {
            backIB.performClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   menuInflater.inflate(R.menu.main, menu)
        return true
    }

   override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
   }

}
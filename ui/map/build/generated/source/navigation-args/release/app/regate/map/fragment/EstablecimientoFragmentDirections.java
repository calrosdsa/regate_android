package app.regate.map.fragment;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import app.regate.map.R;

public class EstablecimientoFragmentDirections {
  private EstablecimientoFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionEstablecimientoFragmentToMain() {
    return new ActionOnlyNavDirections(R.id.action_establecimientoFragment_to_main);
  }
}
